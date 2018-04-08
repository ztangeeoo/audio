package list.controller;

import list.dto.ResultUtil;
import list.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author ztang
 * @date 11:14 2018/3/27
 */
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload")
    public @ResponseBody
    Object uploadImg(@RequestParam("file") MultipartFile file, String bookId) {
        fileService.Upload(file, bookId);
        return ResultUtil.success();
    }

    @GetMapping(value = "getAudioList/{bookId}")
    public ModelAndView getAudioList(@PathVariable String bookId, Model model) {
        model.addAttribute("list", fileService.getAudioList(bookId).getAudioInfoList());
        return new ModelAndView("/lixue/list");
    }


}
