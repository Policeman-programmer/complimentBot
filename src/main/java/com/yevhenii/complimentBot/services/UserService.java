//package com.yevhenii.complimentBot.services;
//
//import com.yevhenii.complimentBot.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.api.objects.Chat;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static com.yevhenii.complimentBot.Constants.MY_USER_NAME;
//
//@Service
//public class UserService {
//
//    @Autowired
//    FileReaderWriterService frService;
//
//    @Value("${usersFilePath}")
//    private String usersFilePath;
//
//    private List<User> users;
//
//    public void wrightUserToFileIfNotExist(Chat chat) {
//        users = convertToUsers(frService.readLinersFromFile(usersFilePath));
//        Optional<User> me = users.stream().filter(user -> MY_USER_NAME.equals(user.getUserName())).findFirst();
//        if (me.isEmpty()){
//
//        }
//
//    }
//
//    private List<User> convertToUsers(List<String> readLinersFromFile) {
//        return readLinersFromFile.stream().map(str -> {
//            String[] strUser = str.split(":");
//            return new User(strUser[0], Long.getLong(strUser[1]));
//        }).collect(Collectors.toList());
//    }
//
//
//}
