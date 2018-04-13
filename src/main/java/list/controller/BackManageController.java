package list.controller;

import list.dto.PageDTO;
import list.dto.Result;
import list.dto.ResultUtil;
import list.entity.AudioInfo;
import list.entity.BookInfo;
import list.service.BackManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ztang
 * @date 11:14 2018/3/27
 */
@RestController
public class BackManageController {

    @Autowired
    private BackManageService backManageService;

    /**
     * 跳转到添加音频的页面
     */
    @GetMapping(value = "/skipVideo")
    public ModelAndView skip(@RequestParam String bookId) {
        return new ModelAndView("add_video").addObject("bookId", bookId);
    }

    /**
     * 保存添加的音频并返回到音频列表
     */
    @PostMapping(value = "/addVideo")
    public void uploadImg(@RequestParam("file") MultipartFile file, String bookId, String fileTime, HttpServletResponse response) throws IOException {
        backManageService.addVideo(file, bookId, fileTime);
        response.sendRedirect("/audio/videoList?bookId=" + bookId);
    }

    /**
     * 删除音频
     */
    @GetMapping(value = "/removeVideo")
    public void removeVideo(HttpServletResponse response, @RequestParam String bookId, String audioId) throws IOException {
        backManageService.removeVideo(bookId, audioId);
        response.sendRedirect("/audio/videoList?bookId=" + bookId);
    }


    /**
     * 查看音频列表
     */
    @GetMapping(value = "/videoList")
    public ModelAndView listVideo(@RequestParam String bookId, Model model) {
        BookInfo audioList = backManageService.getAudioList(bookId);
        model.addAttribute("videos", audioList.getAudioInfoList());
        model.addAttribute("bookId", bookId);
        return new ModelAndView("/video_list");
    }

    /**
     * 微信音频列表展示页面
     */
    @GetMapping(value = "/getAudioList/{bookId}")
    public ModelAndView getAudioList(@PathVariable String bookId, Model model) {
        List<AudioInfo> audioInfoList = backManageService.getAudioList(bookId).getAudioInfoList();
        if (!ObjectUtils.isEmpty(audioInfoList)) {
            model.addAttribute("list", audioInfoList);
        }
        return new ModelAndView("/lixue/list");
    }

    /**
     * 跳转到添加图书的列表
     */
    @GetMapping(value = "/addBook")
    public ModelAndView addBook() {
        return new ModelAndView("/add_book");
    }

    /**
     * 保存添加的图书并转到首页
     */
    @PostMapping(value = "/lixue/addBook")
    public void addBooks(@RequestParam("bookCover") MultipartFile bookCover
            , HttpServletRequest request, HttpServletResponse response) throws IOException {
        backManageService.addBook(request, bookCover);
        response.sendRedirect("/audio/homeList?pageNumber=1&pageSize=10");
    }

    /**
     * 跳转到修改图书的列表
     */
    @GetMapping(value = "/changeBook")
    public ModelAndView changeBook(@RequestParam String bookId) {
        return new ModelAndView("/change_book").addObject("book", backManageService.findby(bookId));
    }

    /**
     * 保存修改的图书并转到首页
     */
    @PostMapping(value = "/lixue/changeBook")
    public void changeBooks(@RequestParam("bookCover") MultipartFile bookCover
            , HttpServletRequest request, HttpServletResponse response) throws IOException {
        backManageService.changeBook(request, bookCover);
        response.sendRedirect("/audio/homeList?pageNumber=1&pageSize=10");
    }

    /**
     * 删除音频
     */
    @GetMapping(value = "removeBook")
    public void removeBook(HttpServletResponse response, @RequestParam String bookId) throws IOException {
        backManageService.removeBook(bookId);
        response.sendRedirect("/audio/homeList?pageNumber=1&pageSize=10");
    }

    /**
     * 管理员登陆后展示的首页
     */
    @GetMapping(value = "/homeList")
    public ModelAndView listHome(Model model, @RequestParam Integer pageNumber, Integer pageSize) {
        PageDTO pageDTO = new PageDTO(pageNumber, pageSize);
        List<BookInfo> books = backManageService.findListBook(pageDTO);
        ArrayList<Integer> pageList = backManageService.countPage(pageDTO);
        if (!ObjectUtils.isEmpty(books)) {
            model.addAttribute("books", books);
            model.addAttribute("pageList", pageList);
        }
        return new ModelAndView("/home");
    }

    /**
     * 生成二维码
     */
    @GetMapping("/lixue/getQR")
    public Object getQR(@RequestParam String bookId, HttpServletResponse response) {
        backManageService.getQR(bookId, response);
        return ResultUtil.success("二维码生成成功！");
    }

    /**
     * 根据书名查找图书
     */
    @GetMapping("/findBooks")
    public ModelAndView findBooks(@RequestParam String bookName, Model model) {
        PageDTO pageDTO = new PageDTO(1, 10);
        List<BookInfo> books = backManageService.findBooks(bookName);
        ArrayList<Integer> pageList = backManageService.countPage(pageDTO);
        if (!ObjectUtils.isEmpty(books)) {
            model.addAttribute("books", books);
            model.addAttribute("pageList", pageList);
            model.addAttribute("bookName", bookName);
        }
        return new ModelAndView("/home");
    }

    /**
     * 测试接口
     */
    @GetMapping("deleteAll")
    public Result deleteAll() {
        backManageService.deleteAll();
        return ResultUtil.success();
    }

}
