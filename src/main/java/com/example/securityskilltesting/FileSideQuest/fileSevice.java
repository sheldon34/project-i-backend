//package com.example.securityskilltesting.Service.IMP;
//
//import com.example.securityskilltesting.FileSideQuest.FileEntity;
//import com.example.securityskilltesting.FileSideQuest.FileRepo;
//import lombok.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//
//public class fileSevice {
//    private FileRepo fileRepo;
//    private FileEntity fileEntity;
//
//    private final String FOLDER_PATH = "Users\\Admin\\OneDrive\\Desktop\\ssFile";
//
//    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
//
//        String filePath = FOLDER_PATH + file.getOriginalFilename();
//
//        FileEntity fileEntity = fileRepo.save(
//                FileEntity.builder()
//                        .name(file.getOriginalFilename())
//                        .type(file.getContentType())
//                        .filepath(filePath)
//                        .build()
//        );
//file.transferTo(new File(filePath));
//
//
//        if (fileEntity != null) {
//            return "file Uploaded successfully" + filePath;
//        }
//        return null;
//    }
//    public byte[] downloadImageFromFileSystem(String fileName){
//        String filePath=fileEntity.getFilepath();
//Optional <FileEntity> dbFile=fileRepo.findByFilename(fileName);
//byte[] images=File.readAllBytes(new File(filePath).toPath());
//return images;
//    }
//}