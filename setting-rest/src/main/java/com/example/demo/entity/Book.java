package com.example.demo.entity;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Book {
  /** ID */
  private Long id;

  /** タイトル */
  @NotNull private String title;

  /** 国際標準図書番号 */
  @NotNull private String isbn;

  /** 公開年 */
  @NotNull private String publicationYear;

  /** 出版社ID */
  private Long pubId;

  /** (関連)出版社情報 */
  private Publisher publisher;

  /** (関連)著者情報 */
  private List<Author> authors;
}
