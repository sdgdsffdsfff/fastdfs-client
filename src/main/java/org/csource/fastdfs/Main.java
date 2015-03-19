package org.csource.fastdfs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by he.wc on 2014/12/6.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(60000);
        socket.connect(new InetSocketAddress("10.0.30.166", 22122));
        byte[] header;

        final byte cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
        header = ProtoCommon.packHeader(cmd, 0, (byte) 0);
        System.out.println(new String(header));

        OutputStream out = socket.getOutputStream();
        out.write(header);
        ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(socket.getInputStream(),
                ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);

        int ipPortLen = pkgInfo.body.length - (ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + 1);
        final int recordLength = ProtoCommon.FDFS_IPADDR_SIZE - 1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
        int serverCount = ipPortLen / recordLength;
        String ip_addr;
        StorageServer[] results = new StorageServer[serverCount];
        byte store_path = pkgInfo.body[pkgInfo.body.length - 1];
        int offset = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
        int port;

        for (int i = 0; i < serverCount; i++) {
            ip_addr = new String(pkgInfo.body, offset, ProtoCommon.FDFS_IPADDR_SIZE - 1).trim();
            offset += ProtoCommon.FDFS_IPADDR_SIZE - 1;

            port = (int) ProtoCommon.buff2long(pkgInfo.body, offset);
            offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

            results[i] = new StorageServer(ip_addr, port, store_path);
            System.out.println(ip_addr + "  "+port + "  "+store_path );
        }

        out.close();
        socket.close();


    }
}
