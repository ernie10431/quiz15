package com.example.quiz15.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz15.constants.QuestionType;
import com.example.quiz15.constants.ResCodeMassage;
import com.example.quiz15.dao.QuestionDao;
import com.example.quiz15.dao.QuizDao;
import com.example.quiz15.entity.Quiz;
import com.example.quiz15.service.ifs.QuizSrevice;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.QuestionVo;
import com.example.quiz15.vo.QuizCreateReq;
import com.example.quiz15.vo.QuizUpdateReq;
import com.example.quiz15.vo.SearchReq;
import com.example.quiz15.vo.SearchRes;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class QuizServiceImpl implements QuizSrevice {

	// 提供 類別(或 Json 格式)與物件之間的轉換
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	/**
	 * @throws Exception
	 * @Transactional: 事務</br>
	 *                 當一個方法中執行多個 Dao 時(跨表或是同意張表寫多筆資料，這些所有的資料都算一次的行為，
	 *                 所以這些資料要嘛全部寫入成功，不然就全部寫入失敗
	 * @Transactional 有效回朔異常預設是 RunTimeException，若發生的異常不是 RunTimeException
	 *                或其子類別的異常類型，資料皆不會回朔，因此想要讓發生任何異常都回朔， 就要將 @Transactional 的有效範圍升格成
	 *                Exception
	 */
	@Transactional(rollbackOn = Exception.class) // 讓執行不是全成功就是全失敗
	@Override
	public BasicRes create(QuizCreateReq req) throws Exception {
		// 參數檢查已透過 @Valid 驗證完成
		try {
			// 檢查日期 :使用排除法
			BasicRes checkRes = checkDate(req.getStartTime(), req.getEndTime());
			if (checkRes.getCode() != 200) {
				return checkRes;
			}

			// 雖然因為 @Transactional 尚未將資料提交(commit)進資料庫，但實際上SQL語法已經執行完畢，
			// 依然可以取得對應的值
			// 新增問卷
			quizDao.insert(req.getName(), req.getDescription(), req.getStartTime(), //
					req.getEndTime(), req.isPublish());
			// 新增完問卷後，取的問卷的流水號
			int quizId = quizDao.getLatestQuizId();
			// 新增問題
			// 取出問卷中的所有問題
			List<QuestionVo> questionList = req.getQuestionList();
			// 處理每一個問題
			for (QuestionVo vo : questionList) {
				// 檢查題目類型與選項
				// 怕太長 另外寫方法
				checkRes = checkQuestionType(vo);
				// 呼叫 checkQuestionType 方法如果得到 成功(200) 代表已成功檢查完成
				if (checkRes.getCode() != 200) {
					// 因為前面已經執行了 quizDao.insert 了，所以這邊要拋出 Exception
					// 才會讓 @Transactional 生效
					throw new Exception(checkRes.getMessage());
				}

				// 因為 MySQL 沒有 List 的資料格式，
				// 所以要把 options 資料格式 從 List<String> 轉成 String
				List<String> optionList = vo.getOptions();
				String str = mapper.writeValueAsString(optionList);

				// 要記得設定 quizId
				questionDao.insert(quizId, vo.getQuestionId(), vo.getQuestion(), //
						vo.getType(), //
						vo.isRequired(), str);
			}

			return new BasicRes(ResCodeMassage.SUCCESS.getCode(), //
					ResCodeMassage.SUCCESS.getMessage());

		} catch (Exception e) {
			// 不能 return BasicRes 而是要將發生的異常拋出去，這樣 @Transaction 才會生效
			throw e;

		}

	}

	private BasicRes checkQuestionType(QuestionVo vo) {
		// 檢查 Type 是否是規定的類型
		String type = vo.getType();
//		System.out.println(""+type);
		if (!type.equalsIgnoreCase(QuestionType.SINGLE.getType())//
				&& !type.equalsIgnoreCase(QuestionType.MULTI.getType())//
				&& !type.equalsIgnoreCase(QuestionType.TEXT.getType()))//
		{
			return new BasicRes(ResCodeMassage.QUESTION_TYPE_ERROR.getCode(),
					ResCodeMassage.QUESTION_TYPE_ERROR.getMessage());
		}
		// 2. type 選項至少有兩個，除了簡答題
		// 假設 type 不等於 TEXT --> 就表示 type 是單選或多選
		if (!type.equalsIgnoreCase(QuestionType.TEXT.getType())) {
			// 選項至少有兩個
			if (vo.getOptions().size() < 2) {
				return new BasicRes(ResCodeMassage.OPTIONS_INSUFFICIENT.getCode(),
						ResCodeMassage.OPTIONS_INSUFFICIENT.getMessage());
			}
		} else {
			// 簡答題選項 不是 null 且 List 有值
			if (vo.getOptions() != null && vo.getOptions().size() > 0) {
				return new BasicRes(ResCodeMassage.TEXT_HAS_OPTIONS_ERROR.getCode(),
						ResCodeMassage.TEXT_HAS_OPTIONS_ERROR.getMessage());
			}
		}
		return new BasicRes(ResCodeMassage.SUCCESS.getCode(), //
				ResCodeMassage.SUCCESS.getMessage());
	}

	private BasicRes checkDate(LocalDate startDate, LocalDate endDate) {
		// 1.開始日期不能比結束日期早 2.開始日期不能早於今天
		// 判斷式: 假設 開始日期比結束日期晚 或 比當時日期早 --> 回傳錯誤
		// LocalDate.now() --> 取得當下時間
		if (startDate.isAfter(endDate)//
				|| startDate.isBefore(LocalDate.now())) {
			return new BasicRes(ResCodeMassage.DATE_FORMAT_ERROR.getCode(),
					ResCodeMassage.DATE_FORMAT_ERROR.getMessage());
		}
		return new BasicRes(ResCodeMassage.SUCCESS.getCode(), //
				ResCodeMassage.SUCCESS.getMessage());
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes update(QuizUpdateReq req) throws Exception {
		// 參數檢查已透過 @Valid 驗證完成

		// 更新是對已存在的問卷進行修改
		try {
			// 1.檢查 quizId 是否存在
			int quizId = req.getQuizId();
			int count = quizDao.getCountByQuizId(quizId);
			if (count != 1) {
				return new BasicRes(ResCodeMassage.NOT_FOUND.getCode(), //
						ResCodeMassage.NOT_FOUND.getMessage());
			}
			// 2.檢查日期
			BasicRes checkRes = checkDate(req.getStartTime(), req.getEndTime());
			if (checkRes.getCode() != 200) {
				return checkRes;
			}

			// 3.更新問卷

			int updateRes = quizDao.update(quizId, req.getName(), req.getDescription(), //
					req.getStartTime(), req.getEndTime(), req.isPublish());
			if (updateRes != 1) { // 表示資料更新失敗
				return new BasicRes(ResCodeMassage.QUIZ_UPDATE_FAILED.getCode(), //
						ResCodeMassage.QUIZ_UPDATE_FAILED.getMessage());
			}

			// 4.刪除同一張問卷的所有問題
			questionDao.deleteByQuizId(quizId);

			// 5.檢查問題
			List<QuestionVo> questionVoList = req.getQuestionList();

			for (QuestionVo vo : questionVoList) {
				// 檢查題目類型與選項
				checkRes = checkQuestionType(vo);
				// 方法如果得到 成功(200) 代表已成功檢查完成
				if (checkRes.getCode() != 200) {
					// 因為前面已經執行了 quizDao.insert 了，所以這邊要拋出 Exception
					// 才會讓 @Transactional 生效
					throw new Exception(checkRes.getMessage());
				}

				// 因為 MySQL 沒有 List 的資料格式，
				// 所以要把 options 資料格式 從 List<String> 轉成 String
				List<String> optionList = vo.getOptions();
				String str = mapper.writeValueAsString(optionList);

				// 要記得設定 quizId
				questionDao.insert(quizId, vo.getQuestionId(), vo.getQuestion(), //
						vo.getType(), vo.isRequired(), str);
			}
		} catch (Exception e) {
			// 不能 return BasicRes 而是要將發生的異常拋出去，這樣 @Transaction 才會生效
			throw e;
		}
		return new BasicRes(ResCodeMassage.SUCCESS.getCode(), //
				ResCodeMassage.SUCCESS.getMessage());
	}

	@Override
	public SearchRes getAllQuizs() {
		List<Quiz> list = quizDao.getAll();
		return new SearchRes(ResCodeMassage.SUCCESS.getCode(), //
				ResCodeMassage.SUCCESS.getMessage(), list);

	}

	@Override
	public SearchRes searach(SearchReq req) {
		// 轉換 Req 的值
		// 若 quizName 是 null，轉成空字串
		String quizName = req.getQuizName();
		if (quizName == null) {
			quizName = "";
		} else {// 多餘的，為了解釋 3元運算子寫的
			quizName = quizName;
		}
		// 3元運算子:
		// 格式: 變數名稱 = 條件判斷式 ? 判斷式結果為 true 時要賦予的值 : 條件判斷式 ? 判斷式結果為 false 時要賦予的值
		quizName = quizName == null ? "" : quizName;

		// 上面程式碼可用下面一行解決
		String quizName1 = req.getQuizName() == null ? "" : req.getQuizName();
		// ===============================
		// 轉換 開始時間 若沒有給開始日期 --> 將日期訂在很早的日期
		LocalDate startDate = req.getStartDate() == null ? LocalDate.of(1970, 1, 1) //
				: req.getStartDate();
		// 轉換 結束時間 若沒有給結束日期 --> 將日期訂在很晚的日期
		LocalDate endDate = req.getEndDate() == null ? LocalDate.of(2999, 12, 31) //
				: req.getEndDate();

		// 搜尋
		List<Quiz> list = quizDao.getAll(quizName, startDate, endDate);
		return new SearchRes(ResCodeMassage.SUCCESS.getCode(), //
				ResCodeMassage.SUCCESS.getMessage(), list);

	}
}
