package com.codersgate.ticketraider.global.infra.s3

import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.*

@Service
class S3Service(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val s3Client: S3Client
) {
    fun putObject(file: MultipartFile): String {
        val key = "${UUID.randomUUID()}${file.originalFilename}"
        val originName = file.originalFilename
        val ext = originName!!.substring(originName.lastIndexOf(".") + 1)
        val metadata = ObjectMetadata()
            .let{ it.contentType="image/$ext" }
        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(file.contentType)
            .contentLength(file.size)
            .build()
        runCatching {
            s3Client.putObject(request, RequestBody.fromBytes(file.bytes))
        }.onFailure { throw RuntimeException("이미지 파일 업로드에 실패했습니다.") }

        return getObjectUrl(key)
    }

    fun getObjectUrl(key: String): String {
        return s3Client.utilities().getUrl(
            GetUrlRequest.builder()
                .bucket(bucket)
                .key(key)
                .build()
        ).toString()
    }

}