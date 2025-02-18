/*
 * Licensed to Apache Software Foundation (ASF) under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Apache Software Foundation (ASF) licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.eventmesh.runtime.util;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public class RemotingHelper {
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static Logger logger = LoggerFactory.getLogger(RemotingHelper.class);

    public static String exceptionSimpleDesc(final Throwable e) {
        StringBuilder sb = new StringBuilder();
        if (e != null) {
            sb.append(e);

            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                StackTraceElement elment = stackTrace[0];
                sb.append(", ");
                sb.append(elment.toString());
            }
        }

        return sb.toString();
    }

    public static SocketAddress string2SocketAddress(final String addr) {
        int split = addr.lastIndexOf(":");
        String host = addr.substring(0, split);
        String port = addr.substring(split + 1);
        InetSocketAddress isa = new InetSocketAddress(host, Integer.parseInt(port));
        return isa;
    }

    //public static RemotingCommand invokeSync(final String addr, final RemotingCommand request,
    //                                         final long timeoutMillis) throws InterruptedException, RemotingConnectException,
    //        RemotingSendRequestException, RemotingTimeoutException {
    //    long beginTime = System.currentTimeMillis();
    //    SocketAddress socketAddress = RemotingUtil.string2SocketAddress(addr);
    //    SocketChannel socketChannel = RemotingUtil.connect(socketAddress);
    //    if (socketChannel != null) {
    //        boolean sendRequestOK = false;
    //
    //        try {
    //
    //            socketChannel.configureBlocking(true);
    //
    //            //bugfix  http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4614802
    //            socketChannel.socket().setSoTimeout((int) timeoutMillis);
    //
    //            ByteBuffer byteBufferRequest = request.encode();
    //            while (byteBufferRequest.hasRemaining()) {
    //                int length = socketChannel.write(byteBufferRequest);
    //                if (length > 0) {
    //                    if (byteBufferRequest.hasRemaining()) {
    //                        if ((System.currentTimeMillis() - beginTime) > timeoutMillis) {
    //
    //                            throw new RemotingSendRequestException(addr);
    //                        }
    //                    }
    //                } else {
    //                    throw new RemotingSendRequestException(addr);
    //                }
    //
    //                Thread.sleep(1);
    //            }
    //
    //            sendRequestOK = true;
    //
    //            ByteBuffer byteBufferSize = ByteBuffer.allocate(4);
    //            while (byteBufferSize.hasRemaining()) {
    //                int length = socketChannel.read(byteBufferSize);
    //                if (length > 0) {
    //                    if (byteBufferSize.hasRemaining()) {
    //                        if ((System.currentTimeMillis() - beginTime) > timeoutMillis) {
    //
    //                            throw new RemotingTimeoutException(addr, timeoutMillis);
    //                        }
    //                    }
    //                } else {
    //                    throw new RemotingTimeoutException(addr, timeoutMillis);
    //                }
    //
    //                Thread.sleep(1);
    //            }
    //
    //            int size = byteBufferSize.getInt(0);
    //            ByteBuffer byteBufferBody = ByteBuffer.allocate(size);
    //            while (byteBufferBody.hasRemaining()) {
    //                int length = socketChannel.read(byteBufferBody);
    //                if (length > 0) {
    //                    if (byteBufferBody.hasRemaining()) {
    //                        if ((System.currentTimeMillis() - beginTime) > timeoutMillis) {
    //
    //                            throw new RemotingTimeoutException(addr, timeoutMillis);
    //                        }
    //                    }
    //                } else {
    //                    throw new RemotingTimeoutException(addr, timeoutMillis);
    //                }
    //
    //                Thread.sleep(1);
    //            }
    //
    //            byteBufferBody.flip();
    //            return RemotingCommand.decode(byteBufferBody);
    //        } catch (IOException e) {
    //            log.error("invokeSync failure", e);
    //
    //            if (sendRequestOK) {
    //                throw new RemotingTimeoutException(addr, timeoutMillis);
    //            } else {
    //                throw new RemotingSendRequestException(addr);
    //            }
    //        } finally {
    //            try {
    //                socketChannel.close();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    } else {
    //        throw new RemotingConnectException(addr);
    //    }
    //}

    public static String parseChannelRemoteAddr(final Channel channel) {
        if (null == channel) {
            return "";
        }
        SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }

            return addr;
        }

        return "";
    }

    public static String parseSocketAddressAddr(SocketAddress socketAddress) {
        if (socketAddress != null) {
            final String addr = socketAddress.toString();

            if (addr.length() > 0) {
                return addr.substring(1);
            }
        }
        return "";
    }

}

