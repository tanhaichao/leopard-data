package io.leopard.data.dfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class MockMultipartFile implements MultipartFile {

	private String url;

	public MockMultipartFile(String url) {
		this.url = url;
	}

	@Override
	public String getName() {
		return url;
	}

	@Override
	public String getOriginalFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		return StringUtils.isEmpty(this.getName());
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		// TODO Auto-generated method stub

	}

}
