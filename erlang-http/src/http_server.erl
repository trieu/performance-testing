%% @author trieunt
%% @doc @todo Add description to http_server.


-module(http_server).

%% ====================================================================
%% API functions
%% ====================================================================
-export([main/0]).

main() ->	
	ready(),
    start(9091).

%% ====================================================================
%% Internal functions
%% ====================================================================

ready() -> io:fwrite("starting HTTP server at port 9091 \n").

start(Port) ->
    spawn(fun () -> {ok, Sock} = gen_tcp:listen(Port, [{active, false}]), 
                    loop(Sock) end).

loop(Sock) ->
    {ok, Conn} = gen_tcp:accept(Sock),
    Handler = spawn(fun () -> handle(Conn) end),
	inet:setopts(Conn, [{packet, 0}, binary,
        {nodelay, true}, {active, true}]),
    gen_tcp:controlling_process(Conn, Handler),
    loop(Sock).

handle(Conn) ->
    gen_tcp:send(Conn, response("Hello!")),
    gen_tcp:close(Conn).

response(Str) ->
    B = iolist_to_binary(Str),
    iolist_to_binary(
      io_lib:fwrite(
         "HTTP/1.0 200 OK\nContent-Type: text/html\nContent-Length: ~p\n\n~s",
         [size(B), B])).

