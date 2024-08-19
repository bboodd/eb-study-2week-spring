package com.study.week2.service;


import com.study.week2.dto.FileDto;
import com.study.week2.mapper.PostMapper;
import com.study.week2.vo.FileVo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.study.week2.dto.FileDto.toDto;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final PostMapper postMapper;

    @Value("${filePath}")
    String filePath;

    /**
     * 파일 업로드
     * @param postId
     * @param file
     * @return
     * @throws IOException
     */

    public int uploadAndSaveFile(int postId, MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();

        String extension = originalName.substring(originalName.lastIndexOf("."));

        String saveName = uuid + extension;

        String savePath = filePath + saveName;

        long fileSize = file.getSize();

        FileVo fileVo = FileVo.builder()
                .postId(postId)
                .fileOriginalName(originalName)
                .fileName(saveName)
                .filePath(filePath)
                .fileSize(fileSize)
                .build();

        file.transferTo(new File(savePath));

        int result = saveFile(fileVo);

        return result;
    }

    /**
     * 파일 등록
     * @param fileVo
     * @return
     */

    public int saveFile(FileVo fileVo){
        int result = postMapper.saveFile(fileVo);
        return result;
    }

    /**
     * 파일 목록 조회
     * @param postId
     * @return
     */

    public List<FileDto> findAllFileByPostId(int postId){
        List<FileVo> fileList = postMapper.findAllFileByPostId(postId);

        List<FileDto> result = fileList.stream()
                .map(f -> toDto(f)).collect(toList());
        return result;
    }

    /**
     * 파일 단건 조회
     * @param fileId
     * @return
     */

    public FileDto findFileById(int fileId){
        FileVo fileVo = postMapper.findFileById(fileId);
        FileDto result = toDto(fileVo);
        return result;
    }

    public int deleteFile(int fileId){
        int result = postMapper.deleteFileById(fileId);
        return result;
    }
}
