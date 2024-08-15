package com.study.week2.controller;

import com.study.week2.dto.*;
import com.study.week2.service.FileService;
import com.study.week2.service.PostService;
import com.study.week2.vo.SearchVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.study.week2.vo.CommentVo.toVo;
import static com.study.week2.vo.PostVo.toVo;

@RequiredArgsConstructor
@Controller
@Slf4j
public class BoardController {

    private final PostService postService;
    private final FileService fileService;

    // TODO: requestbody requestpart 공부 / global exception / dto vo 변환 라이브러리 mapper - from to
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public String getPosts(SearchDto searchDto, Model model) {

        List<CategoryDto> categoryList = postService.getCategories();
        List<PostDto> postList = postService.getPosts(SearchVo.toVo(searchDto));

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("postList", postList);
        model.addAttribute("searchDto", searchDto);

        return "board";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable int postId, Model model){
        int visit = postService.upViewCount(postId);
        PostDto postDto = postService.getPost(postId);
        List<CommentDto> commentList = postService.getComments(postId);
        List<FileDto> fileList = fileService.getFiles(postId);

        model.addAttribute("postDto", postDto);
        model.addAttribute("commentList", commentList);
        model.addAttribute("fileList", fileList);

        log.info("post : " + postDto);

        return "post";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/add")
    public String savePage(Model model) {
        List<CategoryDto> categoryList = postService.getCategories();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("postDto", new PostDto());
        return "add";
    }

//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add/post")
    public String savePost(@Valid PostDto postDto,
                           @RequestParam(value = "files", required = false)List<MultipartFile> files,
                           Model model,
                           BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()){
            model.addAttribute("postDto", postDto);
            return "add";
        }

        //TODO:비밀번호 확인로직은 서비스에서, 글로벌 예외 컨트롤러

        int resultId = 0;
        if(postDto.getPassword().equals(postDto.getCheckPassword())){
            resultId = postService.savePost(toVo(postDto));

            if(files != null){
                int fileResult = 0;
                for(MultipartFile file : files){
                    fileResult += fileService.uploadFile(resultId, file);
                }
                log.info("등록된 파일 개수: " + fileResult);
            }
        }

        return "redirect:/";
    }

//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/add/comment")
    public String saveComment(@Valid CommentDto commentDto){

        int postId = commentDto.getPostId();

        postService.saveComment(toVo(commentDto));

        return "redirect:/post/"+postId;
    }

//    @ResponseStatus(HttpStatus.OK)
    @GetMapping("download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable int fileId, HttpServletRequest request) throws Exception{
        FileDto fileDto = fileService.getFile(fileId);

        String fileName = fileDto.getFileName();
        String fileOriginalName = fileDto.getFileOriginalName();
        String filePath = fileDto.getFilePath();

        String path = filePath + fileName;

        UrlResource resource = null;
        try {
            resource = new UrlResource("file:" + path);
        } catch (Exception e) {
            log.info("Error : {}", e.getMessage());
            e.getStackTrace();
        }

        String mimeType = request.getServletContext().getMimeType(fileOriginalName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(fileOriginalName, StandardCharsets.UTF_8)
                        .build()
                        .toString())
                .body(resource);
    }

//    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public String deletePost(@RequestParam int postId,
                             @RequestParam String inputPassword) {
        boolean success = false;
        String correctPassword = postService.getPost(postId).getPassword();
        if(!inputPassword.isBlank() && inputPassword.equals(correctPassword)){
            int result = postService.deletePost(postId);
            if(result != 0) success = true;
        }
        if(success){
            return "redirect:/";
        } else{
            return "redirect:/post/"+postId;
        }
    }
}
