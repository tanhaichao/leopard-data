package io.leopard.data.dfs.service.image;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import io.leopard.data.dfs.service.DfsService;

@Service
public class ImageDfsServiceImpl implements ImageDfsService {

	protected Log logger = LogFactory.getLog(this.getClass());

	@Resource
	private DfsService dfsService;

	@Override
	public String save(long uid, String folder, byte[] data, String sizeList, boolean asyncSave) throws IOException {
		if (asyncSave) {
			return this.asyncSave(uid, folder, data, sizeList);
		}
		else {
			final String uri = folder + uuid() + ".jpg";
			this.save(uid, uri, data, sizeList);
			return uri;
		}
	}

	protected String asyncSave(final long uid, final String folder, final byte[] data, final String sizeList) throws IOException {
		final String uri = folder + uuid() + ".jpg";

		new Thread() {
			@Override
			public void run() {
				try {
					save(uid, uri, data, sizeList);
					logger.info("save:" + uri);
				}
				catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			};
		}.start();

		return uri;
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
	}

	protected String save(long uid, String uri, byte[] data, String sizeList) throws IOException {
		dfsService.write(uri, data, uid);
		if (sizeList != null && sizeList.length() > 0) {
			String[] list = sizeList.split(",");
			for (String size : list) {
				size = size.trim();
				// this.small(uid, uri, size, data); //TODO ahai 还没有默认生成缩列图.
			}
		}
		return uri;
	}

}
