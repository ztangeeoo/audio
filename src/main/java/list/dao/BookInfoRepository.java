package list.dao;

import list.entity.BookInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;


public interface BookInfoRepository extends MongoRepository<BookInfo, String> {

    Long countByBookNameContains(String bookName);

}
