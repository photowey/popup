package com.photowey.component.test.delayed.queue.app.component.event;

import lombok.Data;

import java.io.Serializable;

/**
 * {@code EventData}
 *
 * @author photowey
 * @date 2023/01/29
 * @since 1.0.0
 */
@Data
public class EventData implements Serializable {

    private static final long serialVersionUID = 2549156899909401319L;

    private Long id;
}
