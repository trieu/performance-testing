%% @author trieunt
%% @doc @todo Add description to hello.

-module(hello).
-export([hello/0]).

hello() -> io:format("Hello World!~n", []).