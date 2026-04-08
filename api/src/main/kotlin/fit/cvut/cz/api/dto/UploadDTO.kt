package fit.cvut.cz.api.dto

import org.springframework.web.multipart.MultipartFile

class UploadDTO (
    var id: Long,
    val file: MultipartFile,
    val type: UploadType
)