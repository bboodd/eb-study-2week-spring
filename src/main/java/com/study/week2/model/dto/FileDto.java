package com.study.week2.model.dto;

import com.study.week2.model.vo.FileVo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class FileDto {

    int fileId;

    @NotBlank(message = "fileDto에 postId값이 들어오지 않았습니다")
    int postId;

    String fileOriginalName;

    String fileName;

    String filePath;

    long fileSize;

    /**
     * vo객체를 dto객체로
     * 다운로드용 화면에 뿌려주기 위함
     * @param fileVo
     * @return fileDto
     */

    public static FileDto toDto(FileVo fileVo){
        FileDto fileDto = FileDto.builder()
                .fileId(fileVo.getFileId())
                .postId(fileVo.getPostId())
                .fileOriginalName(fileVo.getFileOriginalName())
                .build();
        return fileDto;
    }
}
