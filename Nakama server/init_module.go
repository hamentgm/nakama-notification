package main

import (
	"context"
	"database/sql"
	"errors"
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
	_ = initializer.RegisterRpc("getNotification", SendNotificationRPC)

	logger.Info("SERVER STARTED")
	return nil
}

func SendNotificationRPC(ctx context.Context, logger runtime.Logger, db *sql.DB, nk runtime.NakamaModule, payload string) (string, error) {
	logger.Info("Notification RPC called")
	var userId = ctx.Value("user_id")
	if userId == "" {
		return "", errors.New("UserId is null")
	}
	sendNotification(ctx, nk, logger, userId.(string))
	return "", nil
}

func AfterAuthenticateCustom(ctx context.Context, logger runtime.Logger, db *sql.DB, nk runtime.NakamaModule, out *api.Session, in *api.AuthenticateCustomRequest) error {
	logger.Debug(fmt.Sprintf("user authenticated %s", in.Username))
	return nil
}

// Sends notification
func sendNotification(ctx context.Context, nk runtime.NakamaModule, logger runtime.Logger, userId string) {
	logger.Debug(fmt.Sprintf("sending notification to user Id %s", userId))
	if err := nk.NotificationSend(
		ctx,
		userId,
		"subject",
		map[string]interface{}{
			"ticket_id": userId,
		},
		200,
		"",
		true,
	); err != nil {
		logger.Error("Error sending the auth done notification", err)
	} else {
		logger.Debug(fmt.Sprintf("Notification sent to %s", userId))
	}
}
