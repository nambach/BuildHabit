package io.nambm.buildhabit.table;

public interface BlobService {

    String upload(String containerName, String blobName, byte[] data);
}
