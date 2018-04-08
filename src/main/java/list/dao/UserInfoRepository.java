package list.dao;

import list.entity.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserInfoRepository extends MongoRepository<UserInfo, String> {
    UserInfo findByName(String name);

}
