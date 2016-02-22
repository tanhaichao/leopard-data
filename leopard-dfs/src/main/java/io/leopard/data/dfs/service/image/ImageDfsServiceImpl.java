package io.leopard.data.dfs.service.image;

import java.io.IOException;

import org.springframework.stereotype.Service;

/**
 * 异步实现.
 * 
 * @author 阿海
 *
 */
@Service
public class ImageDfsServiceImpl extends ImageDfsServiceSyncImpl {

	@Override
	public String save(final long uid, final String folder, final byte[] data, final String sizeList) throws IOException {
		final String uri = folder + uuid() + ".jpg";

		new Thread() {
			@Override
			public void run() {
				try {
					saveByUri(uid, uri, data, sizeList);
					logger.info("save:" + uri);
				}
				catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			};
		}.start();

		return uri;
	}
}
