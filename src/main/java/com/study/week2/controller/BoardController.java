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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if(!model.containsAttribute("commentDto")){
            model.addAttribute("commentDto", new CommentDto());
        }
        log.info("post : " + postDto);

        return "post";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/add")
    public String savePage(Model model) {
        List<CategoryDto> categoryList = postService.getCategories();
        model.addAttribute("categoryList", categoryList);

        if(!model.containsAttribute("postDto")){
            model.addAttribute("postDto", new PostDto());
        }
        return "add";
    }

//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add/post")
    public String savePost(@Valid PostDto postDto,BindingResult bindingResult,
                           @RequestParam(value = "files", required = false)List<MultipartFile> files,
                           Model model) throws IOException {

        if(bindingResult.hasErrors() || !postDto.getPassword().equals(postDto.getCheckPassword())){
            if(!postDto.getPassword().equals(postDto.getCheckPassword())) {
                bindingResult.rejectValue("checkPassword", "equal", "입력한 비밀번호와 다릅니다.");
            }
            model.addAttribute("postDto", postDto);
            return "redirect:/add";
        }

        //TODO:글로벌 예외 컨트롤러 // 비밀번호 체크 서비스에서? x 서비스에서는 db정보랑 비교

        int resultId = postService.savePost(toVo(postDto));

        if(files != null){
            int fileResult = 0;
            for(MultipartFile file : files){
                if(file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) continue;
                fileResult += fileService.uploadFile(resultId, file);
            }
            log.info("등록된 파일 개수: " + fileResult);
        }

        return "redirect:/";
    }

//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/add/comment")
    public String saveComment(@Valid CommentDto commentDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes ra){

        int postId = commentDto.getPostId();

        if(bindingResult.hasErrors()){
            ra.addFlashAttribute("commentDto", commentDto);
        } else{
            postService.saveComment(toVo(commentDto));

        }
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

    @PostMapping("/check")
    public String updatePassCheck(@RequestParam int postId,
                             @RequestParam String inputPassword) {
        boolean success = false;
        String correctPassword = postService.getPost(postId).getPassword();
        if(!inputPassword.isBlank() && inputPassword.equals(correctPassword)){
            success = true;
        }
        if(success){
            return "redirect:/update/"+postId;
        } else{
            return "redirect:/post/"+postId;
        }
    }

    @GetMapping("/update/{postId}")
    public String getUpdate(@PathVariable int postId, Model model) {
        PostDto postDto = postService.getPost(postId);
        List<FileDto> fileList = fileService.getFiles(postId);
        List<CategoryDto> categoryList = postService.getCategories();

        if(!model.containsAttribute("postDto")){
            model.addAttribute("postDto", postDto);
        }
        model.addAttribute("fileList", fileList);
        model.addAttribute("categoryList", categoryList);

        return "update";
    }

    @PostMapping("/update")
    public String updatePost(@Valid PostDto postDto, BindingResult bindingResult,
                             @RequestParam(value = "files", required = false)List<MultipartFile> files,
                             Model model, RedirectAttributes ra) throws IOException {

        int postId = postDto.getPostId();
        String correctPassword = postService.getPost(postId).getPassword();

        if(bindingResult.hasErrors() || !postDto.getPassword().equals(correctPassword)){
            if(!postDto.getPassword().equals(correctPassword)){
                bindingResult.rejectValue("password", "equal", "입력한 비밀번호와 다릅니다.");
            }
            ra.addFlashAttribute("postDto", postDto);
            return "redirect:/update/"+postId;
        }

        postService.updatePost(toVo(postDto));

        if(files != null){
            int fileResult = 0;
            for(MultipartFile file : files){
                if(file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) continue;
                fileResult += fileService.uploadFile(postId, file);
            }
            log.info("등록된 파일 개수: " + fileResult);
        }

        int[] removeFileList = postDto.getRemoveFileList();
        if(removeFileList != null){
            int fileResult = 0;
            for(int fileId : removeFileList){
                fileResult += fileService.deleteFile(fileId);
            }
            log.info("삭제된 파일 개수 : " + fileResult);
        }

        return "redirect:/post/"+postId;
    }
}
