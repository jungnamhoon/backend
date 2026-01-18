package com.hkorea.skyisthelimit;

import com.hkorea.skyisthelimit.entity.WrongReason;

public record WeaknessStat(
    WrongReason representative,
    int count
) {

}
