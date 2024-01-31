package com.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.entity.FileStore;
import com.entity.MultiFile;
import com.entity.MyFile;
import com.service.FileStoreService;
import com.util.Base64Utils;
import com.util.MediaTypeUtils;

@Controller
@RequestMapping("ftp")
public class FTPController {

	private static final String DEFAULT_ID = "J9VWJBPIJKQCMFY4F8UM";
	
	@Autowired
	private FileStoreService fileStoreService;

	/**
	 * Upload file
	 */
	@GetMapping("upload")
	public String upload() {
		return "upload";
	}

	@PostMapping("upload")
	public String upload(MyFile myFile) {
		try {
			MultipartFile multipartFile = myFile.getMultipartFile();
			String fileName = multipartFile.getOriginalFilename();
			FileStore fileStore = new FileStore();
			fileStore.setFileName(fileName);
			String content = Base64Utils.encodeToString(multipartFile.getBytes());
			fileStore.setFileContent(content);
			fileStoreService.save(fileStore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "upload";
	}

	@GetMapping("multi-upload")
	public String multiUpload() {
		return "multi-upload";
	}

	@PostMapping("multi-upload")
	public String multiUpload(MultiFile myFile) {
		try {
			MultipartFile[] multipartFiles = myFile.getMultipartFile();
			for (MultipartFile multipartFile : multipartFiles) {
				String fileName = multipartFile.getOriginalFilename();
				FileStore fileStore = new FileStore();
				fileStore.setFileName(fileName);
				String content = Base64Utils.encodeToString(multipartFile.getBytes());
				fileStore.setFileContent(content);
				fileStoreService.save(fileStore);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "multi-upload";
	}

	/**
	 * Download file
	 */
	@GetMapping("download")
	public String downloadView(Model model) {
		List<FileStore> files = fileStoreService.findAll();
		model.addAttribute("files", files);
		return "download";
	}

	@GetMapping("download-file")
	public ResponseEntity<ByteArrayResource> download(@RequestParam(defaultValue = DEFAULT_ID) String fileId) {
		FileStore file = fileStoreService.findByFileId(fileId);
		MediaType mediaType = MediaTypeUtils.fromFileName(file.getFileName());
		// System.out.println("mediaType: " + mediaType);
		// System.out.println("fileName: " + file.getFileName());
		String fileContent = file.getFileContent();
		byte[] data = Base64Utils.decodeToByte(fileContent);
		return ResponseEntity.ok() //
				.contentType(mediaType) //
				.contentLength(data.length) //
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFileName()) //
				.body(new ByteArrayResource(data));
	}

	@GetMapping("download-from-url")
	public String downloadFromUrlView() {
		return "download-from-url";
	}

	@PostMapping("download-from-url")
	public ResponseEntity<Resource> downloadFromUrl(String fileId) throws IOException {
		FileStore file = fileStoreService.findByFileId(fileId);
		MediaType mediaType = MediaTypeUtils.fromFileName(file.getFileName());
		String fileContent = file.getFileContent();
		byte[] data = Base64Utils.decodeToByte(fileContent);
		return ResponseEntity.ok() //
				.contentType(mediaType) //
				.contentLength(data.length) //
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFileName()) //
				.body(new ByteArrayResource(data));
	}

	/**
	 * Download using Ajax
	 */
	@GetMapping(value = "/download-ajax")
	public String downloadAjax(Model model) {
		List<FileStore> files = fileStoreService.findAll();
		model.addAttribute("files", files);
		return "download-ajax";
	}

	@ResponseBody
	@PostMapping(value = "/download-ajax")
	public void downloadAjax(HttpServletResponse response, String fileId) throws Exception {
		try {
			FileStore fileInfo = fileStoreService.findByFileId(fileId);
			String fileContent = fileInfo.getFileContent();

			byte[] fileData = Base64Utils.decodeToByte(fileContent);
			File tempFile = File.createTempFile("tmp_", fileInfo.getFileName());
			Files.write(tempFile.toPath(), fileData);

			// Có thể dùng cách tương tự ở trên. Ở đây dùng TempFile để test
			// guessContentTypeFromStream()
			try (FileInputStream in = new FileInputStream(tempFile)) {
				// Set file to header
				response.setContentType(URLConnection.guessContentTypeFromStream(in));
				response.setContentLength(fileData.length);
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileInfo.getFileName() + "\"");
				FileCopyUtils.copy(in, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				tempFile.deleteOnExit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "/download-ajax-base64")
	public ResponseEntity<?> downloadAjaxBase64(String fileId) throws Exception {
		Map<String, String> map = new HashMap<>();
		FileStore fileInfo = fileStoreService.findByFileId(fileId);
		map.put("filename", fileInfo.getFileName());
		map.put("base64", fileInfo.getFileContent());
		// System.out.println(map);
		return ResponseEntity.ok(map);
	}

	// ???
	protected static void showAllHeaderFields(String downloadUrl) throws IOException {
		URLConnection conn = URI.create(downloadUrl).toURL().openConnection();
		// get all headers
		Map<String, List<String>> map = conn.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ", " + entry.getValue());
		}
		// get and verify the header field
		String fieldValue = conn.getHeaderField("Content-Disposition");
		if (fieldValue == null || !fieldValue.contains("filename=")) {
			// no file name there -> throw exception ...
		}
		// parse the file name from the header field
		String filename = fieldValue.substring(fieldValue.indexOf("filename=") + 9, fieldValue.length());
		System.out.println("FileName: " + filename);
	}

	// ???
	private static String getFileName(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		String fieldValue = conn.getHeaderField("Content-Disposition");
		if (fieldValue == null || !fieldValue.contains("filename=")) {
			return "";
		}
		return fieldValue.substring(fieldValue.indexOf("filename=") + 9, fieldValue.length());
	}

	// ???
	protected static long downloadFile(String downloadUrl) throws IOException {
		// showAllHeaderFields(downloadUrl);
		URL url = URI.create(downloadUrl).toURL();
		try (InputStream is = url.openStream()) {
			byte[] byteArray = IOUtils.toByteArray(is);
			InputStream input = new ByteArrayInputStream(byteArray);
			String mimeType = URLConnection.guessContentTypeFromStream(input);
			MediaType mediaType = MediaTypeUtils.fromMineType(mimeType);
			System.out.println("MimeType: " + mimeType);
			System.out.println("Type: " + mediaType.getType());
			System.out.println("Subtype: " + mediaType.getSubtype());
			String fileName = getFileName(url);
			Path path = Paths.get("download", fileName);
			// Files.write(path, byteArray);
			return Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING);
		}
	}

}
