--
-- Created by IntelliJ IDEA.
-- User: chenlushun
-- Date: 2017/11/2
-- Time: 22:20
-- To change this template use File | Settings | File Templates.
--
local function get_max_seq()
    local longitude = tonumber(KEYS[1])
    local latitude = tonumber(KEYS[2])
    if (1 == redis.call('setnx', key, seq)) then
        redis.call('expire', key, 5)
        return seq
    else
        local prev_seq = redis.call('get', key)

        if (prev_seq < seq) then
            redis.call('set', key, seq)
            return seq
        else
            --[[
                不能直接返回redis.call(\'incr\', key),因为返回的是number浮点数类型,会出现不精确情况。
                注意: 类似"16081817202494579"数字大小已经快超时lua和reids最大数值,请谨慎的增加seq的位数
            --]]
            redis.call('incrby', key, 1)
            return redis.call('get', key)
        end
    end
end

return get_max_seq()