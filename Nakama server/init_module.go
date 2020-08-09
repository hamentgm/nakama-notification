package main

import (
	"context"
	"database/sql"
	"fmt"
	"github.com/heroiclabs/nakama-common/api"
	"github.com/heroiclabs/nakama-common/runtime"

	// NOTE: Do not remove. These are required to pin as direct dependencies with Go modules.
	_ "cloud.google.com/go"
	_ "contrib.go.opencensus.io/exporter/stackdriver"
	_ "github.com/aws/aws-sdk-go/aws/awserr"
	_ "github.com/gofrs/uuid"
	_ "github.com/rogpeppe/go-internal/semver"
	_ "go.uber.org/multierr"
	_ "go.uber.org/zap"
	_ "golang.org/x/net/context"
	_ "golang.org/x/net/idna"
	_ "golang.org/x/sys/unix"
)

func InitModule(ctx context.Context, logger runtime.Logger, db *sql.DB, nk runtime.NakamaModule, initializer runtime.Initializer) error {

	_ = initializer.RegisterAfterAuthenticateCustom(AfterAuthenticateCustom)

	if err := initializer.RegisterMatchmakerMatched(MatchMakerFunction); err != nil {
		logger.Error(fmt.Sprintf("Unable to register: %v", err))
		return err
	}

	logger.Info("SERVER STARTED")
	return nil
}

func AfterAuthenticateCustom(ctx context.Context, logger runtime.Logger, db *sql.DB, nk runtime.NakamaModule, out *api.Session, in *api.AuthenticateCustomRequest) error {
	logger.Debug(fmt.Sprintf("user authenticated %s", in.Username))
	//sendNotification(ctx, nk, logger, "userId")
	return nil
}

func MatchMakerFunction(ctx context.Context, logger runtime.Logger, db *sql.DB, nk runtime.NakamaModule, entries []runtime.MatchmakerEntry) (string, error) {
	logger.Debug("Match Created")
	sendMatchmakerDoneNotification(ctx, nk, logger, entries)

	return "dummy_match_id", nil
}

// Sends matchmaking done notification
func sendMatchmakerDoneNotification(ctx context.Context, nk runtime.NakamaModule, logger runtime.Logger, entries []runtime.MatchmakerEntry) {
	var notifications []*runtime.NotificationSend

	for _, ent := range entries {
		notifications = append(notifications, &runtime.NotificationSend{
			UserID:  ent.GetPresence().GetUserId(),
			Subject: "MATCH_MAKING_DONE",
			Content: map[string]interface{}{
				"ticket_id": ent.GetTicket(),
			},
			Code:       100,
			Sender:     "",
			Persistent: true,
		})
		logger.Debug(fmt.Sprintf("Notification sent to %s", ent.GetPresence().GetUserId()))
	}

	if err := nk.NotificationsSend(ctx, notifications); err != nil {
		logger.Error("Error sending the matchmaker done notification", err)
	}
}

// Sends notification
func sendNotification(ctx context.Context, nk runtime.NakamaModule, logger runtime.Logger, userId string) {
	if err := nk.NotificationSend(
		ctx,
		userId,
		"",
		map[string]interface{}{
			"ticket_id": userId,
		},
		200,
		"",
		true,
	); err != nil {
		logger.Error("Error sending the matchmaker done notification", err)
	} else {
		logger.Debug(fmt.Sprintf("Notification sent to %s", userId))
	}
}
