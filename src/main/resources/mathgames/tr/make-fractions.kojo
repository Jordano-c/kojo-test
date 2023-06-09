// Copyright (C) 2015 Anusha Pant <anusha.pant@gmail.com>
// The contents of this file are subject to
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

// Bülent Başaran (ben@scala.org) Türkçe'ye çevirirken ufak tefek değişiklikler yaptı.

val yy = Yazıyüzü("Sans Serif", 40)
val artalanRengi = Renk(255, 232, 181)

val girdi1 = new ay.Yazıgirdisi("") {
    yazıYüzünüKur(yy)
    sütunSayısınıKur(2)
    yatayDüzeniKur(ay.değişmez.merkez)
    artalanıKur(artalanRengi)
}
val girdi2 = new ay.Yazıgirdisi("") {
    yazıYüzünüKur(yy)
    sütunSayısınıKur(2)
    yatayDüzeniKur(ay.değişmez.merkez)
    artalanıKur(artalanRengi)
}

def tıklayıncaYazıyıSil(yg: ay.Yazıgirdisi[Yazı]) {
    yg.odakDinleyiciEkle(new ay.olay.OdakUyarlayıcısı {
        // odağı kazanınca yazıyı siliverelim
        override def focusGained(o: ay.olay.OdakOlayı) {
            yg.yazıyıKur("")
        }
    })
}
tıklayıncaYazıyıSil(girdi1)
tıklayıncaYazıyıSil(girdi2)

var kesirÇizimi = Resim.yatay(0) // şimdilik
val düğme = ay.Düğme("Kesiri çizelim") {
    try { // dene
        kesirÇizimi.sil()
        ondalıkVeYüzde.sil()
        kesirÇizimi = kesiriÇiz(girdi1.değeri.sayıya, girdi2.değeri.sayıya)
        çiz(kesirÇizimi)
        düğme2.etkinliğiKur(doğru)
    }
    catch {  // "try" içinde bir yanılgı yani bir hata olunca burada yakalarız
        case e: ÇalışmaSırasıKuralDışı => // 'case' yani durum şu ise: Java ve Scala'da en genel yanılgı, kural dışı durum türü
            if (girdi1.değeri.boşMu) {
                satıryaz("Pay boş!")
            }
            else if (girdi1.değeri.sayıyaBelki.boşMu) {
                satıryaz("Pay tam sayı değil.")
            }
            else if (girdi2.değeri.boşMu) {
                satıryaz("Payda boş!")
            }
            else if (girdi2.değeri.sayıyaBelki.boşMu) {
                satıryaz("Payda tam sayı değil.")
            }
            else if (girdi2.değeri.sayıya == 0) {
                satıryaz("Payda 1 ya da daha büyük olmalı")
            }
            else {
                satıryaz("Bir hata var!")
            }
    }
}

def ondalığıVeYüzdeyiYaz(pay: Kesir, payda: Kesir) = {
    var ondalık = pay / payda
    var yüzde = ondalık * 100
    Resim.dizi(
        götür(ta.x + 10, -ta.y - 50) -> Resim.arayüz(ay.Tanıt(f"Ondalık olarak: $ondalık%.2f")),
        götür(ta.x + 10, -ta.y - 70) -> Resim.arayüz(ay.Tanıt(f"Yüzde olarak: $yüzde%.2f"))
    )
}

var ondalıkVeYüzde = Resim.yatay(0) // şimdilik
val düğme2: ay.Düğme = ay.Düğme("Kesiri ondalık ve yüzde olarak görelim") {
    try {  // dene
        ondalıkVeYüzde.sil()
        ondalıkVeYüzde = ondalığıVeYüzdeyiYaz(girdi1.değeri.kesire, girdi2.değeri.kesire)
        çiz(ondalıkVeYüzde)
        düğme2.etkinliğiKur(yanlış)
        düğme.pencereİçindekiOdağıİste()
    }
    catch {  // yanılgıları yakalayıp göz ardı edelim
        case e: ÇalışmaSırasıKuralDışı =>  // 'case' yani durum şu ise: Java ve Scala'da en genel yanılgı, kural dışı durum türü
    }
}

def kesiriÇiz(pay1: Sayı, payda1: Sayı) = Resim {
    val mavimsi = Renk(90, 199, 255)
    var pay = mutlakDeğer(pay1)
    var payda = mutlakDeğer(payda1)
    val tam = if (pay <= payda) {
        0
    }
    else {
        val pay2 = pay
        pay = if (payda == 0) pay2 else pay2 % payda
        if (payda == 0) pay1 else pay1 / payda1
    }
    def paydayıÇiz() {
        // paydaları ayıran çizgiler çemberin yarıçapı
        yinele(payda) {
            ileri(110)
            zıpla(-110)
            sağ(360.0 / payda)
        }
    }
    def payıÇiz() {
        boyamaRenginiKur(mavimsi)
        ileri(110)
        sağ()
        sağ((360.0 / payda) * pay, 110)
        sağ()
        ileri(110)
    }
    if (payda < 1) {
        konumuKur(-150, 0)
        tuvaleYaz("Payda 1 ya da daha büyük olmalı")
    } 
    else {
        if (tam != 0) {
            konumuKur(-5, 160)
            tuvaleYaz(s"$tam")
            konumuKur(-5, 140)
            tuvaleYaz(if (tam > 0) "+" else "-")
            konumuKur(0, 0)
        }
        sağ(90)
        zıpla(110)
        sol(90)
        kalemRenginiKur(siyah)
        sol(360, 110)
        sol()
        zıpla(110)
        sağ()
        konumVeYönüBelleğeYaz()
        payıÇiz()
        konumVeYönüGeriYükle()
        paydayıÇiz()
        zıpla(-110)

        // sağ tarafa uzun bir kule çizelim
        boyamaRenginiKur(renksiz)
        sağ()
        zıpla(160)
        sol()
        yinele(2) {
            ileri(220)
            sağ()
            ileri(50)
            sağ()
        }
        // alttan pay/payda kısmını mavimsi renge boyayalım
        boyamaRenginiKur(mavimsi)
        yinele(2) {
            ileri(220.0 * pay / payda)
            sağ()
            ileri(50)
            sağ()
        }
        // katları (yatay çizgilerle) çizelim
        boyamaRenginiKur(renksiz)
        yinele(payda - 1) {
            ileri(220.0 / payda)
            sağ()
            ileri(50)
            zıpla(-50)
            sol()
        }
    }
}

silVeSakla()
girdi1.girdiOdağıOl()  // klavye girdisini pay olarak okuyalım
val ta = tuvalAlanı
çiz(
    götür(ta.x, ta.y) -> Resim.arayüz(
        ay.Satır(
            ay.Sütun(
                girdi1,
                ay.BölmeÇizgisi(mavi, doğru),
                girdi2
            ),
            düğme,
            düğme2
        )
    )
)

düğme2.etkinliğiKur(yanlış)  // başta etkisiz olsun ikinci düğme
