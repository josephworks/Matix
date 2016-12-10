package de.paxii.clarinet.util.login.mcleaks;

import de.paxii.clarinet.Wrapper;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.util.CryptManager;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.SecretKey;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Created by Lars on 17.09.2016.
 */
public class MCLeaksChannelHandler extends MessageToMessageDecoder<Packet> {
  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, Packet packet, List<Object> list) throws Exception {
    if (Wrapper.getClient().getMcLeaksManager().isUsingMcLeaks() && packet instanceof SPacketEncryptionRequest) {
      this.handleEncryptionPacket(Wrapper.getClient().getMcLeaksManager().getNetworkManager(), (SPacketEncryptionRequest) packet);
      channelHandlerContext.pipeline().remove(this);
    } else {
      list.add(packet);
    }
  }

  private void handleEncryptionPacket(NetworkManager networkManager, SPacketEncryptionRequest encryptionRequest) {
    try {
      final SecretKey secretkey = CryptManager.createNewSharedKey();
      String serverId = encryptionRequest.getServerId();
      PublicKey publickey = encryptionRequest.getPublicKey();
      String encryptionKey = (new BigInteger(CryptManager.getServerIdHash(serverId, publickey, secretkey))).toString(16);
      InetSocketAddress remoteAddress = (InetSocketAddress) networkManager.getRemoteAddress();

      MCLeaksManager mcLeaksManager = Wrapper.getClient().getMcLeaksManager();
      MCLeaksLoginBridge.joinServer(
              mcLeaksManager.getMcLeaksSession(),
              mcLeaksManager.getMcLeaksName(),
              remoteAddress.getHostName() + ":" + remoteAddress.getPort(),
              encryptionKey,
              () -> networkManager.sendPacket(
                      new CPacketEncryptionResponse(secretkey, publickey, encryptionRequest.getVerifyToken()),
                      p_operationComplete_1_ -> networkManager.enableEncryption(secretkey), new GenericFutureListener[0])
      );
    } catch (Exception x) {
      x.printStackTrace();
    }
  }
}
