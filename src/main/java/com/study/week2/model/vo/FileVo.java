package com.study.week2.model.vo;
import com.study.week2.model.dto.FileDto;
import lombok.*;

@Builder
@Value
//객체 외부 생성 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class FileVo {

    private int fileId;

    private int postId;

    private String fileOriginalName;

    private String fileName;

    private String filePath;

    private long fileSize;

    /**
     * dto를 vo로 전환
     * db로 들어감(업로드)
     * @param fileDto
     * @return fileVo
     */

    public static FileVo toVo(FileDto fileDto){
        FileVo fileVo = FileVo.builder()
                .postId(fileDto.getPostId())
                .fileOriginalName(fileDto.getFileOriginalName())
                .fileName(fileDto.getFileName())
                .filePath(fileDto.getFilePath())
                .fileSize(fileDto.getFileSize())
                .build();
        return fileVo;
    }
}
