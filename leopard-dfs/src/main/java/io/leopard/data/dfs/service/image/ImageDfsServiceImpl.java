package io.leopard.data.dfs.service.image;

import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.SystemPropertyUtils;

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
		if (SystemUtils.IS_OS_WINDOWS) {
			return super.save(uid, folder, data, sizeList);
		}
		final String uri = folder + uuid() + ".jpg";
		logger.info("async save:" + uri);
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
		// saveByUri(uid, uri, data, sizeList);
		return uri;
	}
}
