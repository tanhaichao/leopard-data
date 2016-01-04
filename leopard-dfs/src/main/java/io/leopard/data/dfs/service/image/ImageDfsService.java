package io.leopard.data.dfs.service.image;

import java.io.IOException;

public interface ImageDfsService {

	String save(long uid, String folder, byte[] data, String sizeList, boolean asyncSave) throws IOException;

}
