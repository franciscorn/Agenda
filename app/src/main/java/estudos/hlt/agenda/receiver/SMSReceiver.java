package estudos.hlt.agenda.receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.telephony.SmsMessage;
import android.widget.Toast;

import estudos.hlt.agenda.DAO.AlunoDAO;
import estudos.hlt.agenda.R;


public class SMSReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];
        String formato = (String) intent.getSerializableExtra("format");

        SmsMessage sms = SmsMessage.createFromPdu(pdu, formato);

        String telefone = sms.getDisplayOriginatingAddress();
        AlunoDAO alunoDAO = new AlunoDAO(context);


        if (alunoDAO.alunoBoolean(telefone)) {
            Toast.makeText(context, "Novo SMS do aluno " + alunoDAO.alunoByPhone(telefone) + "!!", Toast.LENGTH_SHORT).show();
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.msg);
            mediaPlayer.start();
        }

        alunoDAO.close();

    }

}
