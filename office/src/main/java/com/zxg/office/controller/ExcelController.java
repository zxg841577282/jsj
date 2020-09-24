package com.zxg.office.controller;

import com.zxg.office.util.ExcelToHtmlUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：WGS
 * 微信：w1150111308
 * Q  Q：1150111308
 * 邮箱：gosse0405@163.com
 */
@Controller
@Api
public class ExcelController {

	/**
	 * 上传路径
	 */
	@Value("${uploadPath}")
	private String uploadPath;

	@PostMapping("/upload")
	@ResponseBody
	public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();

		String fileName = file.getOriginalFilename();
		// 上传路径
		File upload = new File(uploadPath);
		if(!upload.exists()) {
			upload.mkdirs();
		}
		try {
			// 像磁盘中写入文件
			file.transferTo(new File(upload.getAbsolutePath() + File.separator + fileName));
			map.put("code", 0);
			map.put("msg", fileName);
		} catch (IOException e) {
			e.printStackTrace();
			map.put("code", 1);
			map.put("msg", "上传失败");
		}  finally {
			return map;
		}
	}

	@GetMapping("/toHtml")
	public void toHtml(HttpServletResponse response, String fileName) {
		File file = new File(uploadPath + fileName);
		try {
			String s = ExcelToHtmlUtil.excelToHtml(file);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
