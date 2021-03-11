package ru.geekbrains.march.chat.server;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {

    public static final String COMMAND_STAT = "/stat";

    // Домашнее задание:
    // 1. Разберитесь с кодом, все вопросы можно писать в комментариях к дз
    // 2. Пусть сервер подсчитывает количество сообщений от клиента
    // 3. Если клиент отправит команду '/stat', то сервер должен выслать клиенту
    // не эхо, а сообщение вида 'Количество сообщений - n'

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен на порту 8189. Ожидаем подключение клиента...");
            Socket socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Клиент подключился");

//            п3 если нужно общее количеcтво сообщений без учета подсчета по каждому отдельному клиенту, то код ниже
//
//            int msgCount = 0;
//            while (true) {
//                String msg = in.readUTF().trim();
//                if (msg.length() > 0) {
//                    if (!msg.toLowerCase().equals(COMMAND_STAT)) {
//                        msg = "ECHO: " + msg;
//                        msgCount++;
//                    } else {
//                        msg = "Количество сообщений - " + msgCount;
//                    }
//                    out.writeUTF(msg);
//                    System.out.println(msg);
//                }
//            }


//            иначе этот + фикс в контроллере, чтобы считать сообщения у каждого клиента по отдельности (но все равно как-то криво)
//            пришлось пробрасывать значение константы клиенту, чтобы он понимал команду и правильно подсчитывал сообщения,
//            иначе если отправлять сообщение тут, то клиент получает сообщение о количестве от сервера и воспринимает его как свое и увеличивает счетчик сообщений
//            пробовала тут создать статическую переменную msgCount, увеличивать ее, когда сервер отправляет эхо и уже на клиенте брать ее значение
//            (чтобы можно было по разным командам подчитывать сообщения всего /total_stat и от каждого клиента /stat), но в переменной msgCount всегда по какой то причине 0.
//
            while (true) {
                String msg = in.readUTF().trim();
                if (msg.length() > 0) {
                    msg = !msg.toLowerCase().equals(COMMAND_STAT) ? "ECHO: " + msg : COMMAND_STAT;
                    out.writeUTF(msg);
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
