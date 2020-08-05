local userid=KEYS[1];
local gcid=KEYS[2];
local gcKey='grabcoupon-'..gcid;
local usersKey='SecKill:'..gcid;
local userExists = redis.call("sismember",usersKey,userid);
if tonumber(userExists)==1 then 
	return 2;
end
local num=redis.call("get",gcKey);
if tonumber(num)<=0 then 
	return 0;
else 
	redis.call("decr",gcKey);
	redis.call("sadd",usersKey,userid);
end
return 1






