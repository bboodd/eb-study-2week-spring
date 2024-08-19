package com.study.week2.controller;

import com.study.week2.dto.*;
import com.study.week2.dto.request.PostRequestDto;
import com.study.week2.dto.response.PagingResponse;
import com.study.week2.dto.response.PostResponseDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<CategoryDto> categoryList = postService.findAllCategory();
        PagingResponse<PostResponseDto> response = postService.findAllPostBySearch(searchDto);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("response", response);
        model.addAttribute("searchDto", searchDto);

        return "list";

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable int postId, Model model){
        int visit = postService.increaseViewCountById(postId);
        PostResponseDto postResponseDto = postService.findPostById(postId);
        List<CommentDto> commentList = postService.findAllCommentByPostId(postId);
        List<FileDto> fileList = fileService.findAllFileByPostId(postId);

        model.addAttribute("postDto", postResponseDto);
        model.addAttribute("commentList", commentList);
        model.addAttribute("fileList", fileList);
        if(!model.containsAttribute("commentDto")){
            model.addAttribute("commentDto", new CommentDto());
        }
        log.info("post : " + postResponseDto);

        return "view";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/write")
    public String savePage(@RequestParam(value = "postId", required = false, defaultValue = "0") final int postId
                           , Model model) {
        List<CategoryDto> categoryList = postService.findAllCategory();
        model.addAttribute("categoryList", categoryList);

        //글 수정
        if(postId != 0){
            if(!model.containsAttribute("postDto")){
                //첫 방문이라면
                PostResponseDto postResponseDto = postService.findPostById(postId);
                model.addAttribute("postDto", postResponseDto);
            }
        }
        //글 등록
        else {
            //TODO:화면에 bindingresult 에러메세지 띄우려면 th:object때문에 넣어줘야하는데 그럼 버튼 로직 바꿔여함
//            if(!model.containsAttribute("postDto")){
//                //첫 방문이라면
//                model.addAttribute("postDto", new PostRequestDto());
//            }
        }
        //첫 방문이 아니라면 post메서드에서 model에 postDto를 넣어서 보내줌

        return "write";
    }

//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/save")
    public String savePost(@Valid PostRequestDto postRequestDto, BindingResult bindingResult,
                           @RequestParam(value = "files", required = false)List<MultipartFile> files,
                           Model model) throws IOException {

        String password = postRequestDto.getPassword();
        String checkPassword = postRequestDto.getCheckPassword();
        MessageDto message = MessageDto.builder()
                .message("게시글 등록이 완료되었습니다.")
                .redirectUri("/")
                .method(RequestMethod.GET)
                .data(null)
                .build();

        if(bindingResult.hasErrors() || !password.equals(checkPassword)){
            if(!password.equals(checkPassword)) {
                bindingResult.rejectValue("checkPassword", "equal", "입력한 비밀번호와 다릅니다.");
            }

            message = MessageDto.builder()
                    .message(bindingResult.getFieldError().toString())
                    .redirectUri("/post/write")
                    .method(RequestMethod.GET)
                    .data(null)
                    .build();

            model.addAttribute("postDto", postRequestDto);
            return showMessageAndRedirect(message, model);
        }

        //TODO:글로벌 예외 컨트롤러 // 비밀번호 체크 서비스에서? x 서비스에서는 db정보랑 비교

        int resultId = postService.savePost(toVo(postRequestDto));

        if(files != null){
            int fileResult = 0;
            for(MultipartFile file : files){
                if(file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) continue;
                fileResult += fileService.uploadAndSaveFile(resultId, file);
            }
            log.info("등록된 파일 개수: " + fileResult);
        }

        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/post/update")
    public String updatePost(@Valid PostRequestDto postRequestDto, BindingResult bindingResult,
                             @RequestParam(value = "files", required = false)List<MultipartFile> files,
                             SearchDto searchDto, Model model, RedirectAttributes ra) throws IOException {

        int postId = postRequestDto.getPostId();
        String correctPassword = postService.findPostPasswordById(postId);
        MessageDto message = MessageDto.builder()
                .message("게시글 수정이 완료되었습니다.")
                .redirectUri("/")
                .method(RequestMethod.GET)
                .data(queryParamsToMap(searchDto))
                .build();

        if(bindingResult.hasErrors() || !postRequestDto.getPassword().equals(correctPassword)){
            if(!postRequestDto.getPassword().equals(correctPassword)){
                bindingResult.rejectValue("password", "equal", "입력한 비밀번호와 다릅니다.");
            }
            ra.addFlashAttribute("postDto", postRequestDto);
            return "redirect:/post/write?postId="+postId;
        }

        postService.updatePost(toVo(postRequestDto));

        if(files != null){
            int fileResult = 0;
            for(MultipartFile file : files){
                if(file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) continue;
                fileResult += fileService.uploadAndSaveFile(postId, file);
            }
            log.info("등록된 파일 개수: " + fileResult);
        }

        int[] removeFileList = postRequestDto.getRemoveFileList();
        if(removeFileList != null){
            int fileResult = 0;
            for(int fileId : removeFileList){
                fileResult += fileService.deleteFile(fileId);
            }
            log.info("삭제된 파일 개수 : " + fileResult);
        }

        return showMessageAndRedirect(message, model);
    }

//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/save/comment")
    public String saveComment(@Valid CommentDto commentDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes ra){

        int postId = commentDto.getPostId();

        MessageDto message = MessageDto.builder()
                .message("댓글 등록이 완료되었습니다.")
                .redirectUri("/post/"+postId)
                .method(RequestMethod.GET)
                .data(null)
                .build();

        if(bindingResult.hasErrors()){
            ra.addFlashAttribute("commentDto", commentDto);
        } else{
            postService.saveComment(toVo(commentDto));

        }
        return showMessageAndRedirect(message, model);
    }

//    @ResponseStatus(HttpStatus.OK)
    @GetMapping("download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable int fileId, HttpServletRequest request) throws Exception{
        FileDto fileDto = fileService.findFileById(fileId);

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
    @PostMapping("/post/delete")
    public String deletePost(@RequestParam int postId, SearchDto searchDto, Model model) {

        int result = postService.deletePostById(postId);

        MessageDto message = MessageDto.builder()
                .message("게시글 삭제이 완료되었습니다.")
                .redirectUri("/")
                .method(RequestMethod.GET)
                .data(queryParamsToMap(searchDto))
                .build();

        return showMessageAndRedirect(message, model);

    }

    //사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(MessageDto messageDto, Model model){
        model.addAttribute("params", messageDto);
        return "common/messageRedirect";
    }

    // 쿼리 스트링 파라미터를 Map에 담아 반환
    private Map<String, Object> queryParamsToMap(final SearchDto queryParams) {
        Map<String, Object> data = new HashMap<>();
        data.put("page", queryParams.getPage());
        data.put("recordSize", queryParams.getRecordSize());
        data.put("pageSize", queryParams.getPageSize());
        data.put("keyword", queryParams.getKeyword());
        data.put("categoryId", queryParams.getCategoryId());
        data.put("startDate", queryParams.getStartDate());
        data.put("endDate", queryParams.getEndDate());
        return data;
    }

}
