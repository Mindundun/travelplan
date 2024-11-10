package com.travel.travelplan.repository.friend;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travelplan.entity.QUser;
import com.travel.travelplan.entity.User;
import com.travel.travelplan.entity.friend.QFriend;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findFriendListByUser(User user) {
        QFriend qFriend = QFriend.friend;
        QUser qUser = QUser.user;

        List<User> list = queryFactory.select(qFriend.userFriend)
                .from(qFriend)
                .join(qUser).on(qFriend.userFriend.eq(qUser)).fetchJoin()
                .where(qFriend.userOwn.eq(user))
                .fetch();
        return list;
    }
}
