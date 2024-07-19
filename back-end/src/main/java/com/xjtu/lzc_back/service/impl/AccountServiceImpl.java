package com.xjtu.lzc_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjtu.lzc_back.entity.po.Account;
import com.xjtu.lzc_back.entity.po.History;
import com.xjtu.lzc_back.entity.po.Character;
import com.xjtu.lzc_back.mapper.AccountMapper;
import com.xjtu.lzc_back.mapper.HistoryMapper;
import com.xjtu.lzc_back.mapper.CharacterMapper;
import com.xjtu.lzc_back.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.xjtu.lzc_back.utils.MailUtil;
import java.util.UUID;

@Slf4j
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    private final AccountMapper accountMapper;
    private final HistoryMapper historyMapper;
    private final CharacterMapper characterMapper;
    private String verifyCode;
    private Account account;
    private Character character = new Character();

    public AccountServiceImpl(AccountMapper accountMapper, HistoryMapper historyMapper, CharacterMapper characterMapper) {
        this.accountMapper = accountMapper;
        this.characterMapper = characterMapper;

        UUID randomUUID = UUID.randomUUID();
        this.verifyCode = randomUUID.toString().replaceAll("-", "").substring(0, 6);
        this.historyMapper = historyMapper;
    }

    public void changeCode(){
        UUID randomUUID = UUID.randomUUID();
        this.verifyCode = randomUUID.toString().replaceAll("-", "").substring(0, 6);
    }

    @Override
    public int signIn(Account account) {
        QueryWrapper<Account> nameWrapper = new QueryWrapper<>();
        QueryWrapper<Account> emailWrapper = new QueryWrapper<>();
        QueryWrapper<Account> foundNameWrapper = nameWrapper.eq("username", account.getUsername());
        QueryWrapper<Account> foundEmailWrapper = emailWrapper.eq("email", account.getEmail());
        Long countName = accountMapper.selectCount(foundNameWrapper);
        long countEmail = accountMapper.selectCount(foundEmailWrapper);
        if(countName == 0)  {
            if(countEmail == 0){
                //baseMapper.insert(account);
                return 2;//success
            }
            else{
                return 0;//email duplicated
            }
        }
        return -1;//username duplicated
    }

    @Override
    public int signUp(Account account) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Account> foundWrapper = queryWrapper.eq("username", account.getUsername());
        Long count = accountMapper.selectCount(foundWrapper);
        if(count != 0){
            QueryWrapper<Account> matchedWrapper = foundWrapper.eq("password", account.getPassword());
            if(accountMapper.selectCount(matchedWrapper)==1){
                Account matchedaccount = accountMapper.selectOne(matchedWrapper);
                //System.out.println(matchedaccount.getId());
                return matchedaccount.getId();//success
            }
            else{
                return 0;//error password
            }
        }
        return -1;//no register_record
    }

    @Override
    public void transfer(Account account) {
        this.account = account;
        this.character.setName("default");
        this.character.setDescription("null");
    }

    @Override
    public int Delete(Account account) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Account> foundWrapper = queryWrapper.eq("username", account.getUsername());
        Long count = accountMapper.selectCount(foundWrapper);
        if(count==1){
            int i = accountMapper.selectOne(foundWrapper).getId();
            baseMapper.delete(foundWrapper);
            return i;
        }
        else return 0;
    }

    @Override
    public int verify(String code, boolean flag) {
        //System.out.println(verifyCode);
        //code = code.substring(code.length()-8,code.length()-2);
        if (verifyCode.equals(code)){
            if(!flag){
                baseMapper.insert(this.account);
                Account lastRaw = accountMapper.selectLastRow();
                //System.out.println(lastRaw.getId());
                this.character.setUser_id(lastRaw.getId());
                this.character.setName("default");
                this.character.setTokens("");
                characterMapper.insert(this.character);
                changeCode();
                return lastRaw.getId();
            }
        }
        changeCode();
        return 0;
    }

    @Override
    public boolean sentCode(Account account) {
        changeCode();
        MailUtil mailUtil = new MailUtil(account.getEmail(), verifyCode);
        mailUtil.run();
        return mailUtil.sendMail;
    }
}
