insert into publisher (name) values
('アイカム'),
('飯塚書店'),
('うなぎ書房'),
('エフエー出版');

insert into author (last_name,first_name) values 
('さとう','たける'),
('たなか','ごろう'),
('あべ','しんぞう'),
('あべ','まお'),
('みしま','へいはち'),
('さくら','すみれ'),
('ほんだ','みらい'),
('たなか','たける');

insert into book (title,isbn,publication_year,pub_id) values
('ゆめをかなえるぞう','651324212','2017',1),
('楽して痩せる方法１','721323322','2016',4),
('楽して痩せる方法２','876543213','2014',1),
('買いたい新書','098716352','2017',2),
('新卒アルティマニア','632741352','2015',1),
('メタモルフォーゼ','018982171','2014',2),
('USBの表裏を見分ける方法100','134686541','2015',3),
('いつやるの？いまでしょ','347326213','2016',4),
('よそはよそ。うちはうち','276543123','2017',1),
('誰がために鐘は鳴る','981271651','2018',2),
('小足見てから昇竜余裕','721654212','2013',null);

insert into writing (book_id,author_id) values
(1,1),
(1,2),
(2,5),
(3,4),
(3,7),
(4,2),
(5,8),
(6,2),
(6,6),
(7,3),
(8,4),
(8,1),
(9,1),
(10,2),
(11,1),
(11,2),
(11,3),
(11,4);