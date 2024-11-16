package com.travel.travelplan.repository.friend;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
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
    public List<User> findFriendListByUser(User user, Pageable pageable, String search) {
        QFriend qFriend = QFriend.friend;
        QUser qUser = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        if (search != null && !search.isEmpty()) {
            builder.and(qUser.nickName.contains(search));
        }

        JPAQuery<User> query = queryFactory.select(qFriend.userFriend)
                .from(qFriend)
                .join(qUser).on(qFriend.userFriend.eq(qUser)).fetchJoin()
                .where(qFriend.userOwn.eq(user).and(builder));

        List<String> validSortProperties = Arrays.asList("nickName");

        for(Sort.Order order : pageable.getSort()) {
            if (!validSortProperties.contains(order.getProperty())) {
                continue;
            }
            PathBuilder<User> pathBuilder = new PathBuilder<>(qUser.getType(), qUser.getMetadata());
            query.orderBy(order.isAscending()
                    ? pathBuilder.getString(order.getProperty()).asc()
                    : pathBuilder.getString(order.getProperty()).desc());
        }

        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<User> list = query.fetch();
        return list;
    }

    @Override
    public Integer countFriendListByUser(User user, String search) {
        QFriend qFriend = QFriend.friend;
        QUser qUser = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFriend.userOwn.eq(user));
        builder.and(qFriend.userFriend.isUsed.eq(true));

        if (search != null && !search.isEmpty()) {
            builder.and(qUser.nickName.contains(search));
        }

        return queryFactory.select(qFriend)
                .from(qFriend)
                .join(qUser).on(qFriend.userFriend.eq(qUser)).fetchJoin()
                .where(builder)
                .fetch().size();
    }
}
