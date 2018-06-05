package com.nong.nongo2o.module.personal.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentAgreementBinding;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;

/**
 * Created by PANYJ7 on 2018-5-2.
 */

public class AgreementFragment extends RxBaseFragment {

    public static final String TAG = "AgreementFragment";

    private FragmentAgreementBinding binding;

    public static AgreementFragment newInstance() {
        return new AgreementFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgreementBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        ((IdentifyActivity)getActivity()).setToolbarTitle("用户协议");
        ((TextView)binding.getRoot().findViewById(R.id.tvAgreement)).setText(agreement);
    }

    private String agreement = "本协议由您与佛山市顺德区易见花海信息服务有限公司（以下简称小背篓）共同缔结，本协议具有法律效力。下文中小背篓亦指小背篓APP、小背篓小程序、易见花海微信服务号、易见花海网站等软件产品集合或其中之一。\n" +
            "一、原则条款\n" +
            "A、本协议内容包括协议正文及小背篓发布的各类规则。所有规则为本协议不可分割的组成部分，与本协议具有同等法律效力。除另行声明以外，任何小背篓用户均受本协议约束。\n" +
            "B、您应当在使用小背篓前认真阅读全部协议内容，对于协议中加粗字体显示的内容，您更应该重点阅读。如您对协议有任何疑问，应向小背篓咨询。\n" +
            "C、无论您事实上是否在使用小背篓之前认真阅读了本协议内容，只要您使用小背篓，则本协议将对您产生约束，届时您不应以未阅读本协议内容或者未得到咨询解答等理由，主张本协议无效，或者要求撤销本协议。\n" +
            "D、小背篓有权根据需要不断制定、修改本协议及各类规则，并以通过小背篓进行公告，不再单独通知您。变更后的协议规则一经发布立即自动生效。如您不同意相关变更，应当立即停止使用小背篓服务。若您继续使用小背篓服务，则表示您接受经修订的协议。\n" +
            "二、注册\n" +
            "A、注册者的资格。\n" +
            "在您完成注册或通过其他小背篓允许的方式登录使用小背篓服务时，您应当是具备完全民事权利能力和完全民事行为能力的自然人、法人或者其他组织。若您不具备前述主体资格，则您及您的监护人应承担由此导致的一切后果，且小背篓有权注销（永久冻结）您的小背篓账户，并向您及您的监护人索偿。\n" +
            "B、账户。\n" +
            "小背篓采用完全授权微信登录方式，即微信账号为小背篓账号，手机微信登录成功即可以登录对应的小背篓账户。\n" +
            "为方便您使用小背篓服务及小背篓关联公司或者其他组织的服务（以下简称其他服务），您同意并授权小背篓将您在注册、使用服务过程中提供、形成的信息传递给向您提供服务的小背篓关联公司或者其他组织，或从提供其他服务的的小背篓关联公司或者其他组织获取您在注册、使用其他服务期间提供、形成的信息。\n" +
            "您对您的账户行为负全部责任。如果发现任何人不当使用您的账户或者有其他任何危及您的账户安全的情形时，您应当立即以有效的手段通知小背篓，要求停止相关服务。在小背篓采取有效阻止措施前的一切损失，小背篓均不予承担。\n" +
            "C、会员\n" +
            "在您按照注册提示填写信息、阅读并同意本协议、完成全部注册程序后，或以其他小背篓允许的方式实际使用小背篓服务时，您即成为小背篓会员（简称会员）。\n" +
            "在注册时，您应当按照法律法规要求，按页面提示准确提供并及时更新您的资料，以使之真实、及时、完整和准确。如有合理理由怀疑您提供的资料错误、不实、过时或者不完整的，小背篓有权向您发出询问及/或要求改正的通知，并有权直接做出删除相应资料的处理，直至中止、终止对您提供服务。小背篓不对此承担任何责任，一切直接或者间接产生的支出都由您全部承担。\n" +
            "如因您的联系方式填写不准确或更新不及时，且由此无法和您取得联系而导致您在使用小背篓服务过程中产生任何损失或增加费用的，应由您完全独立承担。\n" +
            "您在使用小背篓服务过程中，所产生的应纳税赋、以及一切硬件、软件、服务及其它方面的费用，均由您独立承担。\n" +
            "三、小背篓服务\n" +
            "A、通过小背篓及其关联公司提供的服务和其他服务，会员可以在小背篓上查询商家和服务信息、达成交易意向并进行实际交易、对其他会员进行评价、参与小背篓组织的活动以及使用其他信息服务和技术服务。\n" +
            "B、您通过小背篓的消费过程中与其他会员发生消费纠纷时，一旦您或者其他会员任一方或双方提交小背篓要求调处，则小背篓有权根据单方判断做出调处决定，您了解并同意接受小背篓的判断和调处决定。该决定将对您具有法律约束力。\n" +
            "C、您了解并同意，小背篓有权应政府部门（包括司法及行政部门）的要求，向其提供您在小背篓填写的注册信息和交易记录等必要信息。如您涉嫌侵犯他人知识产权，则小背篓亦有权在初步判断涉嫌侵权行为存在的情况下，向权利人提供您必要的身份信息。\n" +
            "D、您在小背篓完成交易若干天后，您可以把账户中可提现余额转入您微信零钱账户，转入过程小背篓会按提取额的2%扣收交易管理费，此交易管理费用由小背篓收取并自行使用。\n" +
            "四、小背篓服务使用规范\n" +
            "A、通过小背篓使用小背篓服务过程中，您承诺遵守以下约定：\n" +
            "1、在使用小背篓服务过程中实施的所有行为均遵守国家法律、法规等规范性文件及小背篓各项规则、规定和要求，不违背社会公共利益或公共道德，不损害他人的合法权益，不违反本协议及相关规则。您如违反前述承诺，产生任何法律后果的，您应以自己的名义独立承担所有的法律责任，并确保小背篓免于因此产生的任何损失。\n" +
            "2、在与其他会员交易过程中，遵守诚实信用原则，不采取不正当竞争行为，不扰乱网上交易的正常秩序，不从事与网上交易无关的行为。\n" +
            "3、不发布国家禁止销售的或限制销售的商品或者服务信息（除非取得合法且足够的许可），不发布涉嫌侵犯他人知识产权或者其他合法权益的商品或服务信息，不发布违背社会公共利益或者公共道德或者小背篓认为不适合在网上销售的商品或者服务信息，不发布任何涉嫌违法或违反本协议及各类规则的信息。\n" +
            "4、不以虚构或者歪曲事实的方式不当评价其他会员，不采取不正当方式制造或者提高（降低）自身的信用度，不采取不正当方式制造或者提高（降低）其他会员的信用度。\n" +
            "5、不对小背篓的任何数据做商业性利用，包括但不限于在未经小背篓书面同意的情况下以复制、传播等任何方式使用小背篓展示的资料。\n" +
            "6、不适用任何装置、软件或者理性程序干预或者试图干预小背篓的正常运作或正在小背篓上运行的任何交易、活动。您不得采取任何将导致不合理的庞大数据负载加于小背篓网络设备的行动。\n" +
            "B、您了解并同意\n" +
            "1、小背篓有权对您是否违反上述承诺做出单方面认定，并根据单方认定结果适用规则予以处理或终止向您提供服务、且无须征得您的同意或提前通知于您。\n" +
            "2、经国家行政或司法机关的生效法律文书确认您存在违法或者侵权行为，或者小背篓根据自身的判断，认为您的行为涉嫌违反本协议/或规则的条款或者涉嫌违反法律法规的规定的，则小背篓有权在小背篓上公示您的该等涉嫌违法或者违约行为及小背篓对您采取的措施。\n" +
            "3、对于您在小背篓上发布的涉嫌违法或者涉嫌侵犯他人合法权利或者违反本协议和/或规则的信息、小背篓有权不经通知您即予以删除、且按规则规定进行处罚。\n" +
            "4、对于您在小背篓上实施的行为，包括您未在小背篓上实施但已经对小背篓及用户产生影响的行为，小背篓有权单方认定您行为的性质是否构成对本协议/规则的违反，并据此作出相应处罚。您应自行保存与您行为有关的全部证据，并应对无法提供充分必要证据而承担全部后果。\n" +
            "5、对于您涉嫌违反承诺的行为对任意第三方造成损害的，您均应当以自己的名义独立承担所有法律责任，并应确保小背篓免于因此产生损失或者增加费用。\n" +
            "6、如您涉嫌违反有关法律或者本协议之规定，使小背篓遭受任何损失，或者受到任何第三方的索赔，或受到任何行政管理部门的处罚，您应当赔偿小背篓因此造成的损失及（或）发生的费用，包括合理的律师费用。\n" +
            "五、特别授权\n" +
            "您完全理解并不可撤销地授权小背篓及其关联公司下列权利：\n" +
            "A、一旦您向小背篓及（或）其关联公司（包括联盟商家）等作出任何形式的承诺，且相关公司已经确认您违反了该承诺，则小背篓有权立即按您的承诺或者协议约定的方式对您的账户采取限制措施，包括中止或者终止向您提供服务，并公示相关公司确认的您的违约情况。您了解并同意，小背篓无须就相关确认与您核对事实，或另行征得您的同意，且小背篓无须就此限制措施或者公示行为向您承担任何的责任。\n" +
            "B、一旦您违反本协议，或与小背篓签订的其他协议的约定，小背篓有权以任何方式通知小背篓关联公司，要求其对您的权益采取限制措施，包括将您的账户内的款项支付给小背篓指定的对象，要求关联公司中止、终止对您提供部分或者全部服务，且在其经营或者实际控制的任何网站公示您的违约情况。\n" +
            "C、对于您提供的资料及数据信息，您授予小背篓及其关联公司独家的、全球通用的、永久的、免费的许可使用权利（并有权在多个层面对该权利进行再授权）。此外，小背篓及其关联公司有权使用、复制、修订、改写、发布、翻译、分发、执行和展示您的全部资料数据（包括但不限于注册资料、交易行为数据及全部展示于小背篓的各类信息）或制作其派生作品，并以现在已知或日后开发的任何形式、媒体或技术，将上述信息纳入其他作品内。\n" +
            "六、责任范围和责任限制\n" +
            "A、小背篓负责按“现状”和“可得到”的状态向您提供小背篓服务，但小背篓对小背篓服务不做任何明示或者暗示的保证，包括但不限于小背篓服务的适用性、没有错误或者疏漏、持续性、准确性、可靠性、适用于某一个特定用途。同时，小背篓也不对小背篓服务所涉及的技术及信息的有效性、准确性、正确性、可靠性、质量、稳定、完整和及时性作出任何承诺和保证。\n" +
            "B、您了解小背篓上的信息系会员用户自行发布，且可能存在风险和瑕疵。小背篓仅作为信息交流平台。小背篓仅作为沟通您与商家的一个平台、一座桥梁，但小背篓无法控制交易所涉及的物品的质量、安全或者合法性、商贸信息的真实性或者准确性，以及消费各方履行其在贸易协议中的各项义务的能力。您应自行谨慎判断确定相关物品及/或信息的真实性、合法性和有效性，并自行承担因此产生的责任与损失。\n" +
            "C、除非法律法规明确要求，或出现以下情况，否则，小背篓没有义务对所有用户的注册数据、商品（服务）信息、交易行为以及与交易有关的其他事项进行事先审查：\n" +
            "1、小背篓有合理的理由认定特定会员及具体交易事项可能存在重大违法或违约情形。\n" +
            "2、小背篓有合理的理由认为用户在小背篓上的交易行为涉嫌违法或不当。\n" +
            "D、小背篓及其关联公司有权受理您及其他会员因消费产生的争议，并有权判断与该争议相关的事实及适用的规则，进而作出处理决定。该处理决定对您有约束力。如您在商家消费未能正常支付，小背篓在与您及商家确定数据后，有权通过系统后台扣除您应该支付的款项给商家。\n" +
            "E、您理解并同意，小背篓及其关联公司并非司法机构，仅能以普通人的身份对证据进行鉴别，小背篓及其关联公司对争议的调处完全是基于您的委托，小背篓及其关联公司无法保证争议处理结果符合您的期望，也不对争议调处结论承担任何责任。如您因此遭受损失，您同意自行向受益人索偿。\n" +
            "F、您了解并同意，小背篓不对因下述任一情况而导致您的任何损害赔偿承担责任，包括但不限于利润、商誉、使用、数据等方面的损失或其他无形损失的损害赔偿（无论小背篓是否已被告知该等损害的可能性）：\n" +
            "1、使用或未能使用小背篓服务。\n" +
            "2、第三方未经批准的使用您的账户或更改您的数据。\n" +
            "3、通过小背篓服务购买或获取任何商品、样品、数据、信息或进行交易等行为或替代行为产生的费用及损失。\n" +
            "4、您对小背篓服务的误解。\n" +
            "5、任何非因小背篓的原因而引起的与小背篓服务有关的其他损失。\n" +
            "G、不论在何种情况下，小背篓均不对由于互联网正常的设备维护、互联网连接故障、电脑、通讯或其他系统的故障、电力故障、罢工、劳动争议、暴乱、起义、骚乱、生产力或生产资料不足、火灾、洪水、风暴、爆炸、战争、政府行为、司法行政机关的命令或第三方的不作为而造成的不能服务或者延迟服务承担责任。\n" +
            "七、协议终止\n" +
            "A、您同意，小背篓有权自行全权决定以任何理由不经事先通知的中止、终止向您提供部分或者全部服务，暂时冻结或者永久冻结（注销）您的账户，且无须为此向您或者任何第三方承担任何责任。\n" +
            "B、出现以下情况时，小背篓有权直接以注销账户的方式终止本协议：\n" +
            "1、小背篓终止向您提供服务后，您涉嫌再一次直接或间接以他人名义注册为小背篓用户的。\n" +
            "2、您提供的手机号、电子邮箱不存在或者无法接受电子邮件，且没有其他方式可以与您进行联系，或者小背篓以其他方式通知您更改手机号，而您在小背篓通知后三个工作日内仍未更改为有效的手机号的。\n" +
            "3、您注册信息中的主要内容不真实、不准确、不及时、不完整。\n" +
            "4、本协议（含规则）变更时，您明示并通知小背篓不接受新的服务协议。\n" +
            "5、其他小背篓认为应当终止服务的情况。\n" +
            "C、您有权向小背篓要求注销您的账户，经小背篓审核同意的，小背篓注销（永久冻结）您的账户，届时，您与小背篓基于本协议的合同关系即将终止。您的账户被注销（永久冻结）后，小背篓没有义务为您保留或者向您披露您账户中的任何信息，也没有义务向您或第三方转发任何您未曾阅读或者发送过的信息。\n" +
            "D、您同意，您于小背篓的合同关系终止后，小背篓仍将享有下列权利：\n" +
            "1、继续保存您的注册信息及您使用小背篓服务期间的所有信息。\n" +
            "2、您在使用小背篓服务期间存在违法行为或者违反本协议和/或规则的行为的，小背篓仍可以依据本协议向您主张权利。\n" +
            "八、法律适用、管辖与其他\n" +
            "A、本协议之效力、解释、变更、执行与争议均适用中华人民共和国法律，如无相关法律规定的，则应参照通用国际商业管理和（或）行业惯例。\n" +
            "B、因本协议产生之争议，应依照中华人民共和国法律予以处理，并以小背篓所在地的人民法院为第一审管辖法院。\n";
}