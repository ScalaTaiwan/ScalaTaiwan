import zio.*
import zio.aws.core.AwsError
import zio.aws.core.config.AwsConfig
import zio.aws.netty.NettyHttpClient
import zio.aws.s3.S3
import zio.aws.s3.model.GetObjectRequest
import zio.aws.s3.model.primitives.ObjectKey

object AwsS3 extends ZIOAppDefault:

  val prg: ZIO[S3, AwsError, Unit] =
    for
      buckets <- S3.listBuckets().flatMap(_.getBuckets)
      firstBucket <- buckets.head.getName // don't do it

      res <- S3.getObject(
        GetObjectRequest(bucket = firstBucket, key = ObjectKey("object"))
      )

      contentType <- res.response.getContentType
      bytes <- res.output.runCollect
    yield ()

  def run = prg.provide(AwsConfig.default, NettyHttpClient.default, S3.live)
