package org.csource.fastdfs;

import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by he.wc on 2014/12/6.
 */
public class Main1 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(60000);
        socket.connect(new InetSocketAddress("10.0.30.166", 22122));
        byte[] header = new byte[10];

        final byte cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
        //header = ProtoCommon.packHeader(cmd, 0, (byte) 0);
        header[8]=cmd;
        header[9]=(byte)0;

        OutputStream out = socket.getOutputStream();
        out.write(header);

        socket.getInputStream();

        InputStream in = socket.getInputStream();
        in.read(header);
        long pkg_len = ProtoCommon.buff2long(header, 0);
        System.out.println(pkg_len);

        int totalBytes = 0;
		int remainBytes = (int)pkg_len;
		int bytes;
        byte[] body = new byte[(int)pkg_len];

        while (totalBytes < pkg_len)
		{
			if ((bytes=in.read(body, totalBytes, remainBytes)) < 0)
			{
				break;
			}

			totalBytes += bytes;
			remainBytes -= bytes;
		}
        System.out.println(new String(body));
        String ipAddr = new String(body,  ProtoCommon.FDFS_GROUP_NAME_MAX_LEN,ProtoCommon.FDFS_IPADDR_SIZE - 1).trim();
        long port = ProtoCommon.buff2long(body,ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + ProtoCommon.FDFS_IPADDR_SIZE - 1);
        System.out.println(ipAddr+":"+port);


        out.close();
        socket.close();


    }
}
