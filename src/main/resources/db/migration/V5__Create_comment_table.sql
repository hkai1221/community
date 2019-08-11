create table comment
(
	id bigint auto_increment,
	parent_id bigint not null,
	type int not null,
	commentator bigint not null,
	gmt_create int not null,
	gmt_modified int not null,
	like_count bigint default 0,
	constraint comment_pk
		primary key (id)
);