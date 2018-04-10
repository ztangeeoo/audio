package list.controller;

import list.dto.PageDTO;
import list.dto.ResultUtil;
import list.entity.BookInfo;
import list.service.BackManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ztang
 * @date 11:14 2018/3/27
 */
@RestController
public class BackManageController {

    @Autowired
    private BackManageService backManageService;

    @PostMapping(value = "/upload")
    public @ResponseBody
    Object uploadImg(@RequestParam("file") MultipartFile file, String bookId) {
        backManageService.Upload(file, bookId);
        return ResultUtil.success();
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
    public @ResponseBody
    ModelAndView addBooks(@RequestParam("bookCover") MultipartFile bookCover
            , HttpServletRequest request) {
        backManageService.addBook(request, bookCover);
        return new ModelAndView("/home");
    }

    @GetMapping(value = "/list_video")
    public ModelAndView listVideo() {

        return new ModelAndView("/add_book");
    }

    @GetMapping(value = "/list_home")
    public ModelAndView listHome(Model model, PageDTO pageDTO) {
        Page<BookInfo> listBook = backManageService.findListBook(pageDTO);
        model.addAttribute("pagebook", listBook);
        return new ModelAndView("/home");
    }
}
