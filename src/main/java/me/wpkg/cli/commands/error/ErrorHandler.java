package me.wpkg.cli.commands.error;

public class ErrorHandler
{
    Errors error;

    public enum Errors
    {
        OK("0"),ERROR("1"),SESSION_EXPIRED(""), NOT_AUTHORIZED("NOT_AUTHORIZED");
        public final String code;

        public Runnable event = () -> {};
        Errors(String code)
        {
            this.code = code;
        }

        public void executeEvent()
        {
            event.run();
        }
    }

    private Errors byCode(String code)
    {
        if (code.equals(Errors.OK.code))
            return Errors.OK;
        else if (code.equals(Errors.ERROR.code))
            return Errors.ERROR;
        else if (code.equals(Errors.NOT_AUTHORIZED.code))
            return Errors.NOT_AUTHORIZED;
        return null;
    }

    public void setSessionExpiredEvent(Runnable runnable)
    {
        Errors.SESSION_EXPIRED.event = runnable;
    }

    public void setNotAuthorizedEvent(Runnable runnable)
    {
        Errors.NOT_AUTHORIZED.event = runnable;
    }

    public Errors get()
    {
        return error;
    }

    public boolean ok()
    {
        return error == Errors.OK;
    }

    public boolean notOk()
    {
        return !ok();
    }

    public boolean error()
    {
        return error == Errors.ERROR;
    }

    public void clear()
    {
        error = null;
    }

    public String check(String message)
    {

        //parse serverd message about not joined
        if (message.contains("Client ") && message.contains("not joined. Unknown command."))
            error = Errors.SESSION_EXPIRED;
        //not have error code = error is OK
        else if (message.indexOf("[") != 0)
        {
            error = Errors.OK;
            return message;
        }
        else error = byCode(message.substring(message.indexOf("[") + 1, message.indexOf("]")));

        if (error != null)
            error.executeEvent();

        return message.substring(message.indexOf("]") + 1);
    }
}
