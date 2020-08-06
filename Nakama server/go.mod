module getmega.com/multiplay

go 1.13

replace getmega.com/multiplay/pkg => ./pkg

require golang.org/x/net v0.0.0-20190827160401-ba9fcec4b297

require golang.org/x/sys v0.0.0-20190904154756-749cb33beabd

require contrib.go.opencensus.io/exporter/stackdriver v0.12.7

require github.com/rogpeppe/go-internal v1.3.1

require github.com/gofrs/uuid v0.0.0-20190510204422-abfe1881e60e

require go.uber.org/multierr v1.1.0

require github.com/aws/aws-sdk-go v1.23.16

require (
	cloud.google.com/go v0.45.1
	cloud.google.com/go/pubsub v1.0.1 // indirect
	firebase.google.com/go v3.9.0+incompatible
	github.com/blendle/zapdriver v1.1.6
	github.com/cenkalti/backoff/v3 v3.0.0
	github.com/dghubble/sling v1.3.0
	github.com/gobuffalo/packd v0.3.0
	github.com/gobuffalo/packr v1.30.1
	github.com/golang/protobuf v1.3.2
	github.com/google/uuid v1.1.1 // indirect
	github.com/heroiclabs/nakama-common v1.0.0
	github.com/lithammer/shortuuid v3.0.0+incompatible
	github.com/nelsam/gxui v0.0.0-20191119190801-6c1d5e0eb3bb
	github.com/olekukonko/tablewriter v0.0.4
	github.com/pkg/errors v0.8.1
	github.com/rivo/uniseg v0.1.0
	github.com/spf13/cast v1.3.0
	github.com/spf13/viper v1.3.2
	github.com/twitchtv/twirp v5.8.0+incompatible
	go.opencensus.io v0.22.1
	go.uber.org/atomic v1.4.0 // indirect
	go.uber.org/zap v1.10.0
	google.golang.org/api v0.10.0
)
