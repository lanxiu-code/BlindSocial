const synth = window.speechSynthesis;
function readText(text: string) {
  // 清空之前的朗读
  synth.cancel();
  // 创建一个新的 SpeechSynthesisUtterance 实例
  const utterance = new SpeechSynthesisUtterance(text);
  // 可选：设置语言（例如中文）
  utterance.lang = "zh-CN";
  // 开始朗读
  synth.speak(utterance);
}

export default function (app: any) {
  app.directive("read", {
    mounted(el: HTMLElement) {
      const data = el.getAttribute("data-text") || "";
      el.addEventListener("mouseover", () => {
        readText(data);
      });
      el.addEventListener("focusin", () => {
        readText(data);
      });
      el.addEventListener("mouseleave", () => {
        synth.pause();
      });
    },
  });
}
