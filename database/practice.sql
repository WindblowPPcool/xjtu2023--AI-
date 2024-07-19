-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 删除旧的数据库（如果存在） aigc_virtual_character
DROP DATABASE IF EXISTS practice;
CREATE DATABASE practice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE practice;

-- 用户信息表
DROP TABLE IF EXISTS account;
CREATE TABLE account (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) DEFAULT NULL UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    gender ENUM('Male', 'Female', 'Other') DEFAULT NULL COMMENT '性别',
    nickname VARCHAR(100) DEFAULT NULL COMMENT '昵称',
    birthday DATE DEFAULT NULL COMMENT '生日',
    profession VARCHAR(100) DEFAULT NULL COMMENT '职业',
    region VARCHAR(100) DEFAULT NULL COMMENT '地区',
    hobbies TEXT DEFAULT NULL COMMENT '兴趣爱好',
    signature VARCHAR(255) DEFAULT NULL COMMENT '个性签名',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像',
    role VARCHAR(255) DEFAULT '[2]' COMMENT '角色',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

INSERT INTO account (username, password, email, phone, gender, nickname, birthday, profession, region, hobbies, signature, avatar, role)
VALUES
    ('admin', 'admin', 'admin@example.com', '1234567890', 'Male', 'Admin', '1980-01-01', 'Administrator', '北京', '阅读, 写作', '都是风景，幸会。', '1720595287755', '[1]'),
    ('user1', 'user1', 'user1@example.com', '0987654321', 'Female', 'User1', '1990-05-15', 'Engineer', '西安', '游戏, 编程', '什么是人，欲望满身。', 'li', '[2]'),
    ('user2', 'user2', 'user2@example.com', '1112223333', 'Other', 'User2', '1985-03-22', 'Doctor', '天津', '旅行, 音乐', '永远温柔，与世无仇。', 'Tom', '[2]');

INSERT INTO account (username, password, email, phone, gender, nickname, birthday, profession, region, hobbies, signature, role)
VALUES
    ('user3', 'user3', 'user3@example.com', '1357924680', 'Male', '用户3', '1995-08-20', '学生', '上海', '阅读, 游戏', '山行野宿，孤身万里。', '[2]'),
    ('user4', 'user4', 'user4@example.com', '9876543210', 'Female', '用户4', '1988-11-10', '教师', '上海', '旅行, 美食', '没有期待的日子反而顺顺利利。', '[2]'),
    ('user5', 'user5', 'user5@example.com', '1239876540', 'Male', '用户5', '1992-04-25', '程序员', '上海', '音乐, 跑步', '吹不出褶的平静日子也在闪光。', '[2]'),
    ('user6', 'user6', 'user6@example.com', '4561237890', 'Female', '用户6', '1987-07-15', '设计师', '杭州', '绘画, 手工', '撒娇是察觉到了被偏爱的可能。', '[2]'),
    ('user7', 'user7', 'user7@example.com', '7894561230', 'Male', '用户7', '1998-02-18', '工程师', '西安', '篮球, 游泳', '我们长棱角，因为要变成星星。', '[2]'),
    ('user8', 'user8', 'user8@example.com', '3216549870', 'Female', '用户8', '1993-09-30', '会计师', '西安', '电影, 跳舞', '慢慢体会我的极端与浪漫吧。', '[2]'),
    ('user9', 'user9', 'user9@example.com', '1593574860', 'Male', '用户9', '1990-06-05', '医生', '武汉', '摄影, 钓鱼', '清醒，自律，知进退，明得失。', '[2]'),
    ('user10', 'user10', 'user10@example.com', '6543217890', 'Female', '用户10', '1986-12-12', '艺术家', '上海', '唱歌, 跳绳', '想开一家早餐店，名字就叫早点见面。', '[2]'),
    ('user11', 'user11', 'user11@example.com', '9876541230', 'Male', '用户11', '1997-03-08', '教练', '北京', '篮球, 游戏', '记得给自己的开心充值，别让它停机了。', '[2]'),
    ('user12', 'user12', 'user12@example.com', '4567891230', 'Female', '用户12', '1989-10-20', '作家', '西安', '写作, 唱歌', '走在收藏世界的路上。', '[2]');

-- 虚拟角色信息表
DROP TABLE IF EXISTS virtual_character;
CREATE TABLE virtual_character (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    user_id INT NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    description TEXT DEFAULT NULL COMMENT '角色描述',
    role VARCHAR(255) DEFAULT NULL COMMENT 'role',
    goal VARCHAR(255) DEFAULT NULL COMMENT '目标',
    style VARCHAR(255) DEFAULT NULL COMMENT '样式',
    tokens TEXT DEFAULT NULL COMMENT '总提示词',
    voice VARCHAR(255) DEFAULT NULL COMMENT '语音',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='虚拟角色信息表';

-- 插入数据到虚拟角色信息表
INSERT INTO virtual_character (user_id, name, description, role, goal, style, tokens, voice)
VALUES
    (1, '虚拟助手', '智能助手，可以帮助用户完成各种任务。', '助手', '帮助用户提高工作效率', '现代风格', '你好，有什么可以帮您的吗？', 'female_voice_1'),
    (2, '游戏导师', '专注于游戏攻略和技巧分享。', '导师', '帮助玩家提高游戏水平', '二次元风格', '欢迎来到游戏世界，我是你的导师。', 'male_voice_1'),
    (1, '健身教练', '提供健身建议和锻炼计划。', '教练', '帮助用户达到健身目标', '运动风格', '让我们开始今天的锻炼吧！', 'female_voice_2'),
    (3, '财务顾问', '提供财务建议和投资规划。', '顾问', '帮助用户管理财务', '专业风格', '您的财务问题交给我来处理。', 'male_voice_2'),
    (2, '旅游向导', '推荐旅游景点和旅行路线。', '向导', '让用户的旅行更加愉快', '休闲风格', '准备好开始一段美妙的旅行了吗？', 'female_voice_3');

-- 对话信息表
DROP TABLE IF EXISTS history;
CREATE TABLE history (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '对话ID',
    user_id INT NOT NULL COMMENT '用户ID',
    character_id INT NOT NULL COMMENT '角色ID',
    question TEXT NOT NULL COMMENT '问题',
    answer TEXT NOT NULL COMMENT '回答',
    -- sender TINYINT NOT NULL COMMENT '发送者',-- 0: 用户, 1: 角色
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    FOREIGN KEY (user_id) REFERENCES account(id),
    FOREIGN KEY (character_id) REFERENCES virtual_character(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话信息表';

INSERT INTO history (user_id, character_id, question, answer)
VALUES
    (1, 1, '你好，我想了解一下你们的产品。', '您好，请问有什么具体的问题需要了解的吗？'),
    (2, 1, '你们的服务如何？', '我们的服务包括产品咨询、技术支持和售后服务。'),
    (1, 1, '能否提供一些关于价格的信息？', '当然，请问您对我们的哪个产品感兴趣？'),
    (3, 1, '这个产品有哪些特点？', '我们的产品特点包括高性能、可靠性强、易于使用等。'),
    (2, 1, '你们的公司成立多久了？', '我们公司成立于2010年，专注于提供优质的服务。');

-- 权限菜单表
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
    `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 's-data' COMMENT '图标',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限菜单表' ROW_FORMAT = DYNAMIC;

INSERT INTO `permission` VALUES (1, '用户管理', '用户管理', 'user', 'user-solid');
INSERT INTO `permission` VALUES (2, '虚拟角色管理', '虚拟角色管理', 'book', 's-data');
INSERT INTO `permission` VALUES (3, '对话信息管理', '对话信息管理', 'message', 'message');
INSERT INTO `permission` VALUES (4, '权限管理', '权限管理', 'role', 's-cooperation');
INSERT INTO `permission` VALUES (19, '公告管理', '公告管理', 'notice', 'data-board');
INSERT INTO `permission` VALUES (24, '日志管理', '日志管理', 'log', 'notebook-2');
INSERT INTO `permission` VALUES (25, '角色管理', '角色管理', 'permission', 'menu');

-- 权限管理
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `permission` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限列表',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

INSERT INTO `role` VALUES (1, '管理员', '所有权限', '[1,2,3,4,19,24,27,30]');
INSERT INTO `role` VALUES (2, '普通用户', '部分权限', '[]');

-- 公告表
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
    `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
    `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

INSERT INTO `notice` (`id`, `title`, `content`, `time`) VALUES
(1, '系统维护通知', '尊敬的用户，系统将于今晚进行重要维护，预计影响时间2小时，请提前做好准备。本次维护将对系统的稳定性和性能进行优化，带来更流畅的使用体验。如遇紧急情况，请及时联系客服人员。', '2024-07-18 10:00:00'),
(2, '虚拟角色新产品发布会', '为了迎接新一轮技术革新，本月底将举行新产品发布会，敬请期待精彩内容。新产品将带来更多功能和优化，以满足您对虚拟角色的需求和期待。欢迎您莅临现场，共同见证这一重要时刻！', '2024-07-19 11:00:00'),
(3, '语音交互功能上线', '为了提升用户体验，新增了语音交互功能，欢迎体验和提供反馈。语音交互功能将使得用户与虚拟角色之间的交流更加生动和自然。我们将持续改进和优化这一功能，以满足您的不断增长的需求。', '2024-07-16 14:30:00'),
(4, '暑假安排通知', '各位角色同学，暑假安排如下：7月20日至8月20日为假期，请安排好工作和学习计划。在此期间，虚拟角色将继续为您提供支持和服务。祝愿大家度过一个愉快和充实的假期！', '2024-07-15 08:00:00'),
(5, '周会安排', '本周五下午2点，虚拟角色会议将讨论最新技术进展和市场动态，请准时参加。此次会议是我们交流和协作的重要平台，欢迎大家积极参与，分享您的想法和建议。', '2024-07-16 16:00:00'),
(6, '节假日放假通知', '根据公司规定，即将到来的节假日我们将安排一天的休息时间，放松身心。请大家合理安排时间，享受属于自己的美好时光。虚拟角色团队祝您假期愉快！', '2024-07-17 09:30:00');

-- 日志表
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作内容',
    `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作时间',
    `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作人',
    `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ip',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 admin 登录系统', '2024-07-18 09:54:58', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新角色：管理员', '2024-07-18 10:19:27', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新角色：管理员', '2024-07-18 10:19:29', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 admin 退出系统', '2024-07-18 10:20:05', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 user1 登录系统', '2024-07-18 10:20:09', 'user1', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 user1 退出系统', '2024-07-18 10:20:32', 'user1', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 user2 登录系统', '2024-07-18 10:20:38', 'user2', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user2 ', '2024-07-18 10:20:52', 'user2', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 user2 退出系统', '2024-07-18 10:21:04', 'user2', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 admin 登录系统', '2024-07-18 10:21:07', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('新增用户：user13 ', '2024-07-18 10:22:03', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('删除用户 user13 ', '2024-07-18 10:22:10', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新角色：普通用户', '2024-07-18 10:23:56', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新角色：普通用户', '2024-07-18 10:24:00', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user11 ', '2024-07-18 10:24:10', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user9 ', '2024-07-18 10:24:12', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user7 ', '2024-07-18 10:24:13', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user5 ', '2024-07-18 10:24:15', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user3 ', '2024-07-18 10:24:16', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：admin ', '2024-07-18 10:24:26', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 admin 退出系统', '2024-07-18 10:24:28', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 user1 登录系统', '2024-07-18 10:24:30', 'user1', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user1 ', '2024-07-18 10:24:35', 'user1', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 user1 退出系统', '2024-07-18 10:24:48', 'user1', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 user2 登录系统', '2024-07-18 10:24:49', 'user2', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user2 ', '2024-07-18 10:24:56', 'user2', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('更新用户：user2 ', '2024-07-18 10:25:00', 'user2', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 user2 退出系统', '2024-07-18 10:25:50', 'user2', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 admin 登录系统', '2024-07-18 10:25:52', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 admin 退出系统', '2024-07-18 10:28:41', 'admin', '0:0:0:0:0:0:0:1');
INSERT INTO `log` (`content`, `time`, `user`, `ip`) VALUES ('用户 admin 登录系统', '2024-07-18 10:28:42', 'admin', '0:0:0:0:0:0:0:1');

SET FOREIGN_KEY_CHECKS = 1;
