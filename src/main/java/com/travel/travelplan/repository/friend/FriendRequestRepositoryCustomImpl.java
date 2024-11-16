package com.travel.travelplan.repository.friend;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travelplan.entity.QUser;
import com.travel.travelplan.entity.User;
import com.travel.travelplan.entity.friend.FriendRequest;
import com.travel.travelplan.entity.friend.QFriendRequest;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FriendRequestRepositoryCustomImpl implements FriendRequestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FriendRequest> findFriendRequestListByUser(User requestUser, Pageable pageable, String search) {
        QUser targetUser = QUser.user;
        QFriendRequest qFriendRequest = QFriendRequest.friendRequest;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFriendRequest.requestUser.eq(requestUser));
        builder.and(qFriendRequest.isAccepted.isNull());
        if (search != null && !search.isEmpty()) {
            builder.and(targetUser.nickName.contains(search));
        }

        JPAQuery<FriendRequest> query = queryFactory
                .select(qFriendRequest)
                .from(qFriendRequest)
                // .join(qFriendRequest.requestUser, requestUser).fetchJoin()
                .join(qFriendRequest.targetUser, targetUser).fetchJoin()
                .where(builder);

        // sort 적용
        query.orderBy(qFriendRequest.sentDate.desc());

        // 페이징 적용
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        List<FriendRequest> list = query.fetch();
        return list;
    }

    @Override
    public Integer countFriendRequestListByUser(User requestUser, String search) {
        QUser targetUser = QUser.user;
        QFriendRequest qFriendRequest = QFriendRequest.friendRequest;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFriendRequest.requestUser.eq(requestUser));
        builder.and(qFriendRequest.isAccepted.isNull());
        if (search != null && !search.isEmpty()) {
            builder.and(targetUser.nickName.contains(search));
        }

        JPAQuery<FriendRequest> query = queryFactory
                .select(qFriendRequest)
                .from(qFriendRequest)
                .join(qFriendRequest.targetUser, targetUser)
                .where(builder);

        return query.fetch().size();
    }

    @Override
    public List<FriendRequest> findFriendRequestReceivedListByUser(User targetUser, Pageable pageable, String search) {
        QUser requestUser = QUser.user;
        QFriendRequest qFriendRequest = QFriendRequest.friendRequest;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFriendRequest.targetUser.eq(targetUser));
        builder.and(qFriendRequest.isAccepted.isNull());
        if (search != null && !search.isEmpty()) {
            builder.and(requestUser.nickName.contains(search));
        }

        JPAQuery<FriendRequest> query = queryFactory
                .select(qFriendRequest)
                .from(qFriendRequest)
                .join(qFriendRequest.requestUser, requestUser).fetchJoin()
                .where(builder);

        // sort 적용
        query.orderBy(qFriendRequest.sentDate.desc());

        // 페이징 적용
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        List<FriendRequest> list = query.fetch();
        return list;
    }

    @Override
    public Integer countFriendRequestReceivedListByUser(User targetUser, String search) {
        QUser requestUser = QUser.user;
        QFriendRequest qFriendRequest = QFriendRequest.friendRequest;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFriendRequest.targetUser.eq(targetUser));
        builder.and(qFriendRequest.isAccepted.isNull());
        if (search != null && !search.isEmpty()) {
            builder.and(requestUser.nickName.contains(search));
        }

        JPAQuery<FriendRequest> query = queryFactory
                .select(qFriendRequest)
                .from(qFriendRequest)
                .join(qFriendRequest.requestUser, requestUser)
                .where(builder);

        return query.fetch().size();
    }

}
