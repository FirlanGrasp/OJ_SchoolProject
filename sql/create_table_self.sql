# 测验表
create table if not exists test
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '测试标题',
    content     text                               null comment '测验描述(Markdouwn 格式)',
    startTime   text                           null comment '开始时间(格式: YYYY-MM-DD HH:mm)',
    endTime     text                           null comment '结束时间(格式: YYYY-MM-DD HH:mm)',
    studentVisible        boolean                      null comment '是否对学生可见',
    rankVisible      boolean                               null comment '是否显示排行榜',
    codeShare      boolean                               null comment '是否允许代码分享',
    examType   boolean                          null comment '测验类型(false=测验，true=考试)',
    totalRequiredScore   int                    null comment '总分要求(默认100分)',
    questionsId    text                               null comment '题目id',
    creatUserId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (id)
) comment '测验表' collate = utf8mb4_unicode_ci;

# 测验关联问题表
create table if not exists test_question
(
    id          bigint auto_increment comment 'id' primary key,
    testId      bigint                         not null comment '测验id',
    questionId bigint                          not null comment '题目id',
    title      text                            null comment '题目标题',
    score      int                       null comment '题目分值',
    type     text                               null comment '题目类型',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (id)
) comment '测验关联问题表' collate = utf8mb4_unicode_ci;

alter table user
    add number varchar(255) not null comment '学号，默认密码'  after userAccount;

alter table user
    add constraint user_pk
        unique (number);

ALTER TABLE question
    ADD COLUMN isReviewed BOOLEAN NOT NULL DEFAULT FALSE;

# 5.30-----------------------------------------------------------------------------------------
# 班级表
create table class
(
    id         bigint auto_increment comment '班级ID'
        primary key,
    className  varchar(256)                       not null comment '班级名称',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    constraint class_pk
        unique (className)
)
    comment '班级表' collate = utf8mb4_unicode_ci;

-- 班级-学生关联表
create table class_student
(
    id          bigint auto_increment comment 'ID'
        primary key,
    classId     bigint                             not null comment '班级ID',
    studentId   bigint                             not null comment '学生ID',
    userAccount varchar(256)                       not null comment '学生账号(真实姓名)',
    number      varchar(255)                       not null comment '学号',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    constraint class_student_number_pk
        unique (classId, number),
    constraint class_student_pk
        unique (classId, studentId),
    constraint fk_class_student_classId
        foreign key (classId) references class (id),
    constraint fk_class_student_studentId
        foreign key (studentId) references user (id)
)
    comment '班级学生关联表' collate = utf8mb4_unicode_ci;

