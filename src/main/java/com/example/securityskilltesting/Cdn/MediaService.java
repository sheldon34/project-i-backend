package com.example.securityskilltesting.Cdn;

import com.amazonaws.services.lambda.model.UnsupportedMediaTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final S3Client s3Client;
    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;
    @Value("${cloudflare.r2.public-domain}")
    private String publicDomain;


    public String uploadFile(MultipartFile file) {
        String original= Optional.ofNullable(file.getOriginalFilename()).orElseThrow(
                ()-> new UnsupportedMediaTypeException("File not found"));

        String contentType=Optional.ofNullable(file.getContentType())
                .orElseThrow(()-> new UnsupportedMediaTypeException("file type not found"));

        String ext =getFileExtension(original);
        String folder=switch(ext){
            case "png","jpg","jpeg","gif"->"images";
            case "mp4","mov","avi"->"videos";
            case "mp3","wav"->"audios";
            case "pdf","docx","txt"->"documents";
            default -> throw new UnsupportedMediaTypeException("file type not found"+contentType);
        };
        /// Build Object Key
        String key=String.format("%s/%s-%s",folder, UUID.randomUUID(),original);

        //Prepare s3 put request
        PutObjectRequest req=PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();
        ///  perform upload
        try{
            s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));
        }
        catch (Exception e){
            throw new RuntimeException("Failed to upload file",e);
        }
        return key;
    }

    /// Delete an existing object from R2
    public void deleteFile(String key ){
        DeleteObjectRequest request=DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }
    /// updating an existing object from R2 by deleting the old one
    public String updateFile(String oldKey,MultipartFile newFile){
        if (oldKey !=null &&  !oldKey.isBlank() && newFile!=null){
            try{
                s3Client.deleteObject(DeleteObjectRequest.builder()
                                .bucket(bucketName)
                                .key(oldKey)
                        .build());
            }catch (Exception e){
                throw new RuntimeException("Failed to update file",e);
            }
        }
            return uploadFile(newFile);

    }
    public String getPublicUrl(String key){
        return String.format("%s%s",publicDomain,key);
    }
    private String getFileExtension(String filename){
        int idx= filename.lastIndexOf('.');
        if (idx<0||idx==filename.length()-1){
            throw new RuntimeException("Invalid file extension in filename"+filename);
        }
        return filename.substring(idx+1);
    }

}
