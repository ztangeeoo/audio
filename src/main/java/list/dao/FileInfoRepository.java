package list.dao;

import list.entity.BookInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;


public interface FileInfoRepository extends MongoRepository<BookInfo, String> {

    BookInfo findByBookName(String bookName);

    ArrayList<BookInfo> findByBookId(String bookId);

}
