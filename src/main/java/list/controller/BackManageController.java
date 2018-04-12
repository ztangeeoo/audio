package list.controller;

import list.dto.PageDTO;
import list.dto.ResultUtil;
import list.entity.BookInfo;
import list.service.BackManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ztang
 * @date 11:14 2018/3/27
 */
@RestController
public class BackManageController {

    @Autowired
    private BackManageService backManageService;

    @GetMapping(value = "/skipVideo")
    public ModelAndView skip(@RequestParam String bookId){
        return new ModelAndView("add_video").addObject("bookId",bookId);
    }


    @PostMapping(value = "/addVideo")
    public void uploadImg(@RequestParam("file") MultipartFile file, String bookId,String fileTime,HttpServletResponse response) throws IOException {
        backManageService.addVideo(file, bookId,fileTime);
       // response.sendRedirect("/videoList?bookId="+bookId);
    }

    @GetMapping(value = "/videoList")
    public ModelAndView listVideo(@RequestParam String bookId,Model model) {
        BookInfo audioList = backManageService.getAudioList(bookId);
        model.addAttribute("videos",audioList.getAudioInfoList());
        model.addAttribute("bookId",bookId);
        return new ModelAndView("/video_list");
    }

    @GetMapping(value = "/getAudioList/{bookId}")
    public ModelAndView getAudioList(@PathVariable String bookId, Model model) {
        model.addAttribute("list", backManageService.getAudioList(bookId).getAudioInfoList());
        return new ModelAndView("/lixue/list");
    }

    @GetMapping(value = "/addBook")
    public ModelAndView addBook() {
        return new ModelAndView("/add_book");
    }


    @PostMapping(value = "/lixue/addBook")
    public void addBooks(@RequestParam("bookCover") MultipartFile bookCover
            , HttpServletRequest request, HttpServletResponse response) throws IOException {
        backManageService.addBook(request, bookCover);
        response.sendRedirect("/audio/list_home?pageNumber=1&pageSize=15");
    }



    @GetMapping(value = "/list_home")
    public ModelAndView listHome(Model model, @RequestParam Integer pageNumber, Integer pageSize) {
        PageDTO pageDTO = new PageDTO(pageNumber, pageSize);
        List<BookInfo> books = backManageService.findListBook(pageDTO);
        model.addAttribute("books", books);
        model.addAttribute("pageNumber",pageNumber);
        return new ModelAndView("/home");
    }
}
