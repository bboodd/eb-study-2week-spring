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

    public int uploadFile(int postId, MultipartFile file) throws IOException {

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
        int result = postMapper.insertFile(fileVo);
        return result;
    }

    /**
     * 파일 다운로드
     * @param fileId
     * @param request
     * @param response
     * @throws Exception
     */


    //레거시 다운로드 로직
    public void downloadFile(int fileId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //TODO: 버퍼인풋스트림, 버퍼아웃풋스트림도 있음
        FileDto fileDto = getFile(fileId);

        String originalName = fileDto.getFileOriginalName();
        String fileName = fileDto.getFileName();

        String path = filePath + fileName;
        @Cleanup
        FileInputStream fis = new FileInputStream(path);

        String mimeType = request.getServletContext().getMimeType(originalName);
        if(mimeType == null){
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);

        String encodeFileName = URLEncoder.encode(originalName, StandardCharsets.UTF_8);

        response.setHeader("Content-Disposition", "attachment; filename=" + encodeFileName);

        byte[] buffer = new byte[4096];
        int readBytes = 0;

        @Cleanup
        ServletOutputStream sos = response.getOutputStream();

        while((readBytes = fis.read(buffer)) != -1) {
            sos.write(buffer, 0, readBytes);
        }

        fis.close();
        sos.flush();

        //TODO: finaly 에서 fis닫기
    }

    /**
     * 파일 목록 조회
     * @param postId
     * @return
     */

    public List<FileDto> getFiles(int postId){
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

    public FileDto getFile(int fileId){
        FileVo fileVo = postMapper.findFileByFileId(fileId);
        FileDto result = toDto(postMapper.findFileByFileId(fileId));
        return result;
    }

    public int deleteFile(int fileId){
        int result = postMapper.deleteFileByFileId(fileId);
        return result;
    }
}
