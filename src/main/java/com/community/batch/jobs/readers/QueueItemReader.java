package com.community.batch.jobs.readers;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * ItemReader의 기본 반환 타입은 단수형인데,
 * 그에 따라 구현하면 User객체 1개씩 DB에  select쿼리를 요청하므로 매우 비효율적인 방식이 된다.
 * 따라서 이같은 방법을 사용
 */
public class QueueItemReader<T> implements ItemReader<T> {
    private Queue<T> queue;

    public QueueItemReader(List<T> data) {
        this.queue = new LinkedList<>(data); // 휴면 회원으로 지정된 타깃 데이터를 한번에 불러와 큐에 저장
    }


    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return this.queue.poll();
    }
}